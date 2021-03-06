package com.github.nelsonwilliam.invscp.client.presenter;

import java.awt.event.ActionEvent;
import java.util.List;

import com.github.nelsonwilliam.invscp.client.util.Client;
import com.github.nelsonwilliam.invscp.client.view.OrdemServicoView;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalInsertException;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalUpdateException;
import com.github.nelsonwilliam.invscp.shared.model.dto.FuncionarioDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.OrdemServicoDTO;

public class OrdemServicoPresenter extends Presenter<OrdemServicoView> {

    private final MainPresenter mainPresenter;
    private final OrdensServicoPresenter ordensServicoPresenter;

    public OrdemServicoPresenter(final OrdemServicoView view,
            final MainPresenter mainPresenter,
            final OrdensServicoPresenter ordensServicoPresenter) {
        super(view);
        this.mainPresenter = mainPresenter;
        this.ordensServicoPresenter = ordensServicoPresenter;
        setupViewListeners();
    }

    private void setupViewListeners() {
        view.addConfirmarListener((final ActionEvent e) -> {
            onConfirmar();
        });
    }

    private void onConfirmar() {
        final FuncionarioDTO usuario = mainPresenter.getUsuario();
        final OrdemServicoDTO ordemServicoDTO = view.getOrdemServico();
        if (ordemServicoDTO == null) {
            view.showError("Não foi possível inserir/alterar o item.");
            return;
        }

        if (ordemServicoDTO.getId() == null) {
            onConfirmarAdicao(usuario, ordemServicoDTO);
        } else {
            onConfirmarAtualizacao(usuario, ordemServicoDTO.getId(),
                    ordemServicoDTO);
        }
    }

    private void onConfirmarAdicao(final FuncionarioDTO usuario,
            final OrdemServicoDTO ordemServicoNovo) {

        try {
            Client.requestValidarInserirOrdemServico(usuario, ordemServicoNovo);
        } catch (final IllegalInsertException e) {
            view.showError(e.getMessage());
            return;
        }

        final int addedId = Client.requestAddOrdemServico(ordemServicoNovo);
        final OrdemServicoDTO addedOrdem =
                Client.requestGetOrdemServicoById(addedId);
        view.showSucesso();
        view.close();

        // Executa as pós-inserções e exibe as mensagens resultantes.
        final List<String> messages =
                Client.requestPosInserirOrdemServico(usuario, addedOrdem);
        for (final String message : messages) {
            view.showInfo(message);
        }

        ordensServicoPresenter.updateOrdensServico();
    }

    private void onConfirmarAtualizacao(final FuncionarioDTO usuario,
            final Integer idOrdemServicoAnterior,
            final OrdemServicoDTO ordemServicoAtualizado) {

        try {
            Client.requestValidarAlterarOrdemServico(usuario,
                    idOrdemServicoAnterior, ordemServicoAtualizado);
        } catch (final IllegalUpdateException e) {
            view.showError(e.getMessage());
            return;
        }

        Client.requestUpdateOrdemServico(ordemServicoAtualizado);
        view.showSucesso();
        view.close();
        ordensServicoPresenter.updateOrdensServico();
    }
}
