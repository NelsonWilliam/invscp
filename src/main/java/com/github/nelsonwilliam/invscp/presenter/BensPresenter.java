package com.github.nelsonwilliam.invscp.presenter;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

import com.github.nelsonwilliam.invscp.exception.IllegalDeleteException;
import com.github.nelsonwilliam.invscp.model.dto.BaixaDTO;
import com.github.nelsonwilliam.invscp.model.dto.BemDTO;
import com.github.nelsonwilliam.invscp.model.dto.DepartamentoDTO;
import com.github.nelsonwilliam.invscp.model.dto.FuncionarioDTO;
import com.github.nelsonwilliam.invscp.model.dto.GrupoMaterialDTO;
import com.github.nelsonwilliam.invscp.model.enums.BemSituacaoEnum;
import com.github.nelsonwilliam.invscp.util.Client;
import com.github.nelsonwilliam.invscp.view.BaixaView;
import com.github.nelsonwilliam.invscp.view.BemView;
import com.github.nelsonwilliam.invscp.view.BensView;
import com.github.nelsonwilliam.invscp.view.OrdensServicoView;
import com.github.nelsonwilliam.invscp.view.ViewFactory;

public class BensPresenter extends Presenter<BensView> {

    private final MainPresenter mainPresenter;

    public BensPresenter(final BensView view,
            final MainPresenter mainPresenter) {
        super(view);
        this.mainPresenter = mainPresenter;
        setupViewListeners();
        updateBens();
    }

    private void setupViewListeners() {
        view.addAdicionarBemListener((final ActionEvent e) -> {
            onAdicionarBem();
        });
        view.addDeletarBensListener((final ActionEvent e) -> {
            onDeletarBens();
        });
        view.addAlterarBemListener((final ActionEvent e) -> {
            onAlterarBem();
        });
        view.addOrdemServicoListener((final ActionEvent e) -> {
            onOrdemServico();
        });
        view.addBaixarBemListener((final ActionEvent e) -> {
            onBaixa();
        });
    }

    @SuppressWarnings("unused")
    private void onAdicionarBem() {
        final BemDTO novoBem = new BemDTO();
        novoBem.setSituacao(BemSituacaoEnum.INCORPORADO);
        novoBem.setDataAquisicao(LocalDate.now());
        novoBem.setDataCadastro(LocalDate.now());
        novoBem.setGarantia(LocalDate.now());
        novoBem.setSala(Client.requestGetSalaDeposito());

        final List<DepartamentoDTO> bens =
                Client.requestGetPossiveisDepartamentosParaBem(novoBem);
        final List<GrupoMaterialDTO> grupos =
                Client.requestGetPossiveisGruposMateriaisParaBem(novoBem);

        final BemView bemView = ViewFactory.createBem(mainPresenter.getView(),
                novoBem, true, bens, grupos);
        final BemPresenter bemPresenter =
                new BemPresenter(bemView, mainPresenter, this);
        bemView.setVisible(true);
    }

    private void onDeletarBens() {
        final List<Integer> selectedBemIds = view.getSelectedBensIds();
        if (selectedBemIds.size() < 1) {
            view.showError("Nenhum item foi selecionado.");
            return;
        }

        view.showConfirmacao("Deletar " + selectedBemIds.size() + " bem(ns)?",
                (final Boolean confirmado) -> {
                    if (confirmado) {
                        deletarBens(selectedBemIds);
                    }
                });
    }

    private void deletarBens(final List<Integer> bemIds) {
        final FuncionarioDTO usuario = mainPresenter.getUsuario();

        int deletados = 0;
        for (final Integer idBem : bemIds) {
            try {
                Client.requestValidarDeleteBem(usuario, idBem);
            } catch (final IllegalDeleteException e) {
                view.showError(e.getMessage());
                continue;
            }

            Client.requestDeleteBem(idBem);
            deletados++;
        }

        if (deletados > 0) {
            view.showSucesso("Bem(s) deletado(s) com sucesso.");
        }
        updateBens();
    }

    @SuppressWarnings("unused")
    private void onAlterarBem() {
        final List<Integer> selectedBemIds = view.getSelectedBensIds();
        if (selectedBemIds.size() != 1) {
            throw new RuntimeException(
                    "Para alterar um elemento é necessário que apenas um esteja selecionado.");
        }

        final Integer selectedBemId = selectedBemIds.get(0);
        final BemDTO selectedBem = Client.requestGetBemById(selectedBemId);
        final List<DepartamentoDTO> bens =
                Client.requestGetPossiveisDepartamentosParaBem(selectedBem);
        final List<GrupoMaterialDTO> grupos =
                Client.requestGetPossiveisGruposMateriaisParaBem(selectedBem);

        final BemView bemView = ViewFactory.createBem(mainPresenter.getView(),
                selectedBem, false, bens, grupos);
        final BemPresenter bemPresenter =
                new BemPresenter(bemView, mainPresenter, this);
        bemView.setVisible(true);
    }

    @SuppressWarnings("unused")
    private void onOrdemServico() {
        final List<Integer> selectedBemIds = view.getSelectedBensIds();
        if (selectedBemIds.size() != 1) {
            throw new RuntimeException(
                    "Para ver as ordens de serviço, necessário que apenas um bem esteja selecionado.");
        }

        final Integer selectedBemId = selectedBemIds.get(0);
        final BemDTO selectedBem = Client.requestGetBemById(selectedBemId);

        final OrdensServicoView ordensView =
                ViewFactory.createOrdensServico(mainPresenter.view);
        final OrdensServicoPresenter ordensPresenter =
                new OrdensServicoPresenter(ordensView, mainPresenter, this,
                        selectedBem);
        ordensView.setVisible(true);
    }

    @SuppressWarnings("unused")
    private void onBaixa() {
        final List<Integer> selectedBemIds = view.getSelectedBensIds();
        if (selectedBemIds.size() != 1) {
            throw new RuntimeException(
                    "Para ver baixa, é necessário que apenas um bem esteja selecionado.");
        }

        final Integer selectedBemId = selectedBemIds.get(0);
        final BemDTO selectedBem = Client.requestGetBemById(selectedBemId);
        BaixaDTO baixa = Client.requestGetBaixaByIdBem(selectedBemId);
        if (baixa == null) {
            baixa = new BaixaDTO();
            baixa.setFuncionario(mainPresenter.getUsuario());
            baixa.setData(LocalDate.now());
            baixa.setBem(selectedBem);
        }

        final BaixaView baixaView = ViewFactory.createBaixa(
                mainPresenter.getView(), baixa, baixa.getId() == null);
        final BaixaPresenter baixasPresenter =
                new BaixaPresenter(baixaView, mainPresenter, this);
        baixaView.setVisible(true);
    }

    public void updateBens() {
        final List<BemDTO> bens = Client.requestGetBens();
        view.updateBens(bens);
    }

}
