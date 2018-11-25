package com.github.nelsonwilliam.invscp.presenter;

import java.awt.event.ActionEvent;
import java.util.List;

import com.github.nelsonwilliam.invscp.exception.IllegalInsertException;
import com.github.nelsonwilliam.invscp.exception.IllegalUpdateException;
import com.github.nelsonwilliam.invscp.model.dto.FuncionarioDTO;
import com.github.nelsonwilliam.invscp.model.dto.MovimentacaoDTO;
import com.github.nelsonwilliam.invscp.util.Client;
import com.github.nelsonwilliam.invscp.view.MovimentacaoView;

public class MovimentacaoPresenter extends Presenter<MovimentacaoView> {

    private final MainPresenter mainPresenter;
    private final MovimentacoesPresenter movimentacoesPresenter;
    private final BensPresenter bensPresenter;

    public MovimentacaoPresenter(final MovimentacaoView view,
            final MainPresenter mainPresenter,
            final BensPresenter bensPresenter) {
        super(view);
        this.mainPresenter = mainPresenter;
        movimentacoesPresenter = null;
        this.bensPresenter = bensPresenter;
        setupViewListeners();
    }

    public MovimentacaoPresenter(final MovimentacaoView view,
            final MainPresenter mainPresenter,
            final MovimentacoesPresenter movimentacoesPresenter) {
        super(view);
        this.mainPresenter = mainPresenter;
        this.movimentacoesPresenter = movimentacoesPresenter;
        bensPresenter = null;
        setupViewListeners();
    }

    private void setupViewListeners() {
        view.addConfirmarListener((final ActionEvent e) -> {
            onConfirmar();
        });
    }

    private void onConfirmar() {
        final FuncionarioDTO usuario = mainPresenter.getUsuario();
        final MovimentacaoDTO movDTO = view.getMovimentacao();
        if (movDTO == null) {
            view.showError("Não foi possível inserir/alterar o item.");
            return;
        }

        if (movDTO.getId() == null) {
            onConfirmarAdicao(usuario, movDTO);
        } else {
            onConfirmarAtualizacao(usuario, movDTO.getId(), movDTO);
        }
    }

    private void onConfirmarAdicao(final FuncionarioDTO usuario,
            final MovimentacaoDTO movNovo) {

        // Gera um número de guia de autorização se for para outra cidade
        if (!movNovo.isParaMesmaCidade()) {           
            String guia = Client.requestGerarNumGuiaTransporte();
            if (guia == null) {
                view.showError("Não foi possível gerar uma guia de transporte. Tente novamente.");
                return;
            }
            movNovo.setNumGuiaTransporte(guia);
        }

        try {
            Client.requestValidarInserirMovimentacao(usuario, movNovo);
        } catch (final IllegalInsertException e) {
            view.showError(e.getMessage());
            return;
        }

        Integer newId = Client.requestAddMovimentacao(movNovo);
        movNovo.setId(newId);
        view.showSucesso();
        view.close();

        // Executa as pós-alterações e exibe as mensagens resultantes.
        final List<String> messages =
                Client.requestPosInserirMovimentacao(usuario, movNovo);
        for (final String message : messages) {
            view.showInfo(message);
        }

        if (movimentacoesPresenter != null)
            movimentacoesPresenter.updateMovimentacoes();
        if (bensPresenter != null)
            bensPresenter.updateBens();
    }

    private void onConfirmarAtualizacao(final FuncionarioDTO usuario,
            final Integer idMovAnterior, final MovimentacaoDTO movAtualizado) {

        try {
            Client.requestValidarAlterarMovimentacao(usuario, idMovAnterior,
                    movAtualizado);
        } catch (final IllegalUpdateException e) {
            view.showError(e.getMessage());
            return;
        }

        Client.requestUpdateMovimentacao(movAtualizado);
        view.showSucesso();
        view.close();

        if (movimentacoesPresenter != null)
            movimentacoesPresenter.updateMovimentacoes();
        if (bensPresenter != null)
            bensPresenter.updateBens();
    }
}
