package com.github.nelsonwilliam.invscp.client.presenter;

import java.awt.event.ActionEvent;
import java.util.List;

import com.github.nelsonwilliam.invscp.client.util.Client;
import com.github.nelsonwilliam.invscp.client.view.DepartamentoView;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalInsertException;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalUpdateException;
import com.github.nelsonwilliam.invscp.shared.model.dto.DepartamentoDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.FuncionarioDTO;

public class DepartamentoPresenter extends Presenter<DepartamentoView> {

    private final MainPresenter mainPresenter;
    private final DepartamentosPresenter deptsPresenter;

    public DepartamentoPresenter(final DepartamentoView view,
            final MainPresenter mainPresenter,
            final DepartamentosPresenter deptsPresenter) {
        super(view);
        this.mainPresenter = mainPresenter;
        this.deptsPresenter = deptsPresenter;
        setupViewListeners();
    }

    private void setupViewListeners() {
        view.addConfirmarListener((final ActionEvent e) -> {
            onConfirmar();
        });
    }

    private void onConfirmar() {
        final FuncionarioDTO usuario = mainPresenter.getUsuario();
        final DepartamentoDTO deptDTO = view.getDepartamento();
        if (deptDTO == null) {
            view.showError("Não foi possível inserir/alterar o item.");
            return;
        }

        if (deptDTO.getId() == null) {
            onConfirmarAdicao(usuario, deptDTO);
        } else {
            onConfirmarAtualizacao(usuario, deptDTO.getId(), deptDTO);
        }
    }

    private void onConfirmarAdicao(final FuncionarioDTO usuario,
            final DepartamentoDTO deptNovo) {

        try {
            Client.requestValidarInserirDepartamento(usuario, deptNovo);
        } catch (final IllegalInsertException e) {
            view.showError(e.getMessage());
            return;
        }

        final int addedId = Client.requestAddDepartamento(deptNovo);
        final DepartamentoDTO addedDept =
                Client.requestGetDepartamentoById(addedId);
        view.showSucesso();
        view.close();

        // Executa as pós-alterações e exibe as mensagens resultantes.
        final List<String> messages =
                Client.requestPosAlterarDepartamento(usuario, addedDept);
        for (final String message : messages) {
            view.showInfo(message);
        }

        // Se o departamento do funcionario logado tiver sido alterado, força a
        // reatualização da exibição da tela para este usuário
        if (usuario.getDepartamento() != null && usuario.getDepartamento()
                .getId().equals(addedDept.getId())) {
            mainPresenter.setIdUsuario(usuario.getId());
        }

        deptsPresenter.updateDepartamentos();
    }

    private void onConfirmarAtualizacao(final FuncionarioDTO usuario,
            final Integer idDeptAnterior,
            final DepartamentoDTO deptAtualizado) {

        try {
            Client.requestValidarAlterarDepartamento(usuario, idDeptAnterior,
                    deptAtualizado);
        } catch (final IllegalUpdateException e) {
            view.showError(e.getMessage());
            return;
        }

        Client.requestUpdateDepartamento(deptAtualizado);
        view.showSucesso();
        view.close();

        // Executa as pós-alterações e exibe as mensagens resultantes.
        final List<String> messages =
                Client.requestPosAlterarDepartamento(usuario, deptAtualizado);
        for (final String message : messages) {
            view.showInfo(message);
        }

        // Se o departamento do funcionario logado tiver sido alterado, força a
        // reatualização da exibição da tela para este usuário
        if (usuario.getDepartamento() != null && usuario.getDepartamento()
                .getId().equals(deptAtualizado.getId())) {
            mainPresenter.setIdUsuario(usuario.getId());
        }

        deptsPresenter.updateDepartamentos();
    }
}
