package com.github.nelsonwilliam.invscp.client.view;

import java.awt.event.ActionListener;
import java.util.List;

import com.github.nelsonwilliam.invscp.shared.model.dto.DepartamentoDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.PredioDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.SalaDTO;

public interface SalaView extends View {
    // ----------------------------------------
    // Métodos para notificar ações do usuário.
    // ----------------------------------------

    void addConfirmarListener(ActionListener listener);

    // -------------------------------------------
    // Métodos para atualizar os valores exibidos.
    // -------------------------------------------

    /**
     * Atualiza todos os valores da janela para exibir os dados do predio
     * passado.
     */
    void updateSala(SalaDTO sala, final List<PredioDTO> predios,
            final List<DepartamentoDTO> departamentos);

    /**
     * Exibe uma mensagem de erro com a mensagem especificada. Por exemplo, pode
     * ser chamado para avisar que algum campo obrigatório foi deixado vazio.
     */
    void showError(String message);

    /**
     * Exibe uma mensagem de que o item foi alterado/adicionado com sucesso.
     */
    void showSucesso();

    void showInfo(String message);

    /**
     * Fecha a View.
     */
    void close();

    // ---------------------------------------------
    // Métodos para obter os valores de formulários.
    // ---------------------------------------------

    /**
     * Obtém o departamento com os valores atualmente exibidos no formulário que
     * o usuário pode ou não ter alterado.
     */
    SalaDTO getSala();
}
