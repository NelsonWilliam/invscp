package com.github.nelsonwilliam.invscp.client.view;

import java.awt.event.ActionListener;

import com.github.nelsonwilliam.invscp.shared.model.dto.EventoMovimentacaoDTO;

public interface EventoMovimentacaoView extends View {

    // ----------------------------------------
    // Métodos para notificar ações do usuário.
    // ----------------------------------------

    void addConfirmarListener(ActionListener listener);

    // -------------------------------------------
    // Métodos para atualizar os valores exibidos.
    // -------------------------------------------

    /**
     * Atualiza todos os valores da janela para exibir os dados do evento de
     * movimentação passado.
     */
    void updateEventoMovimentacao(EventoMovimentacaoDTO eventoMovimentacao);

    /**
     * Exibe uma mensagem de erro com a mensagem especificada. Por exemplo, pode
     * ser chamado para avisar que algum campo obrigatório foi deixado vazio.
     */
    void showError(String message);

    void showInfo(String message);

    /**
     * Exibe uma mensagem de que o item foi alterado/adicionado com sucesso.
     */
    void showSucesso();

    /**
     * Fecha a View.
     */
    void close();

    // ---------------------------------------------
    // Métodos para obter os valores de formulários.
    // ---------------------------------------------

    /**
     * Obtém a baixa com os valores atualmente exibidos no formulário que o
     * usuário pode ou não ter alterado.
     */
    EventoMovimentacaoDTO getEventoMovimentacao();
}
