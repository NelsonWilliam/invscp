package com.github.nelsonwilliam.invscp.client.view.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import com.github.nelsonwilliam.invscp.client.view.EventoMovimentacaoView;
import com.github.nelsonwilliam.invscp.shared.model.dto.EventoMovimentacaoDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.FuncionarioDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.MovimentacaoDTO;
import com.github.nelsonwilliam.invscp.shared.model.enums.TipoEventoMovEnum;

public class EventoMovimentacaoSwingView extends JDialog
        implements EventoMovimentacaoView {

    private static final long serialVersionUID = -1119086918035833897L;

    private final boolean isAdicionar;

    private Integer idEventoMovimentacao;
    private FuncionarioDTO funcionario;
    private TipoEventoMovEnum tipo;
    private LocalDate data;
    private MovimentacaoDTO movimentacao;

    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JLabel fieldTipo;
    private JLabel lblTipo;
    private JLabel lblData;
    private JLabel lblJustificativa;
    private JLabel fieldData;
    private JLabel lblFuncionrio;
    private JLabel fieldFuncionario;
    private JTextPane fieldJustificativa;

    /**
     * @param eventoMovimentacao Eventos de movimentações cujos valores serão
     *        exibidos inicialmente.
     * @param isAdicionar Indica se a janela que será exibida será para adição
     *        de uma nova ordem de serviço (true) ou para atualização de um
     *        evento de movimentação existente (false).
     */

    public EventoMovimentacaoSwingView(final JFrame owner,
            final EventoMovimentacaoDTO eventoMovimentacao,
            final boolean isAdicionar) {

        super(owner,
                isAdicionar ? "Adicionar evento de movimentação"
                        : "Ver evento de movimentação",
                ModalityType.APPLICATION_MODAL);
        this.isAdicionar = isAdicionar;
        initialize();
        updateEventoMovimentacao(eventoMovimentacao);
    }

    private void initialize() {
        setBounds(0, 0, 500, 284);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 25, 102, 0, 25 };
        gridBagLayout.rowHeights = new int[] { 25, 0, 0, 0, 99, 25, 0, 25 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0 };
        gridBagLayout.rowWeights =
                new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0 };
        getContentPane().setLayout(gridBagLayout);

        btnConfirmar = new JButton("Confirmar");

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener((final ActionEvent e) -> {
            close();
        });

        lblTipo = new JLabel("Tipo:");
        final GridBagConstraints gbc_lblTipo = new GridBagConstraints();
        gbc_lblTipo.anchor = GridBagConstraints.EAST;
        gbc_lblTipo.insets = new Insets(0, 0, 5, 5);
        gbc_lblTipo.gridx = 1;
        gbc_lblTipo.gridy = 1;
        getContentPane().add(lblTipo, gbc_lblTipo);

        fieldTipo = new JLabel();
        final GridBagConstraints gbc_comboTipo = new GridBagConstraints();
        gbc_comboTipo.insets = new Insets(0, 0, 5, 5);
        gbc_comboTipo.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboTipo.gridx = 2;
        gbc_comboTipo.gridy = 1;
        getContentPane().add(fieldTipo, gbc_comboTipo);

        lblFuncionrio = new JLabel("Funcionário:");
        GridBagConstraints gbc_lblFuncionrio = new GridBagConstraints();
        gbc_lblFuncionrio.anchor = GridBagConstraints.EAST;
        gbc_lblFuncionrio.insets = new Insets(0, 0, 5, 5);
        gbc_lblFuncionrio.gridx = 1;
        gbc_lblFuncionrio.gridy = 2;
        getContentPane().add(lblFuncionrio, gbc_lblFuncionrio);

        fieldFuncionario = new JLabel("");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.anchor = GridBagConstraints.WEST;
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 2;
        gbc_label_1.gridy = 2;
        getContentPane().add(fieldFuncionario, gbc_label_1);

        lblData = new JLabel("Data:");
        final GridBagConstraints gbc_lblData = new GridBagConstraints();
        gbc_lblData.anchor = GridBagConstraints.EAST;
        gbc_lblData.insets = new Insets(0, 0, 5, 5);
        gbc_lblData.gridx = 1;
        gbc_lblData.gridy = 3;
        getContentPane().add(lblData, gbc_lblData);

        fieldData = new JLabel("");
        GridBagConstraints gbc_fieldData = new GridBagConstraints();
        gbc_fieldData.anchor = GridBagConstraints.WEST;
        gbc_fieldData.insets = new Insets(0, 0, 5, 5);
        gbc_fieldData.gridx = 2;
        gbc_fieldData.gridy = 3;
        getContentPane().add(fieldData, gbc_fieldData);

        lblJustificativa = new JLabel("Justificativa:");
        final GridBagConstraints gbc_lblJustificativa =
                new GridBagConstraints();
        gbc_lblJustificativa.anchor = GridBagConstraints.EAST;
        gbc_lblJustificativa.insets = new Insets(0, 0, 5, 5);
        gbc_lblJustificativa.gridx = 1;
        gbc_lblJustificativa.gridy = 4;
        getContentPane().add(lblJustificativa, gbc_lblJustificativa);

        fieldJustificativa = new JTextPane();
        fieldJustificativa
                .setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridBagConstraints gbc_textPane = new GridBagConstraints();
        gbc_textPane.insets = new Insets(0, 0, 5, 5);
        gbc_textPane.fill = GridBagConstraints.BOTH;
        gbc_textPane.gridx = 2;
        gbc_textPane.gridy = 4;
        getContentPane().add(fieldJustificativa, gbc_textPane);

        final GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
        gbc_btnCancelar.anchor = GridBagConstraints.WEST;
        gbc_btnCancelar.insets = new Insets(0, 0, 5, 5);
        gbc_btnCancelar.fill = GridBagConstraints.VERTICAL;
        gbc_btnCancelar.gridx = 1;
        gbc_btnCancelar.gridy = 6;
        getContentPane().add(btnCancelar, gbc_btnCancelar);

        final GridBagConstraints gbc_btnConfirmar = new GridBagConstraints();
        gbc_btnConfirmar.insets = new Insets(0, 0, 5, 5);
        gbc_btnConfirmar.anchor = GridBagConstraints.EAST;
        gbc_btnConfirmar.fill = GridBagConstraints.VERTICAL;
        gbc_btnConfirmar.gridx = 2;
        gbc_btnConfirmar.gridy = 6;
        getContentPane().add(btnConfirmar, gbc_btnConfirmar);
    }

    @Override
    public void addConfirmarListener(final ActionListener listener) {
        btnConfirmar.addActionListener(listener);
    }

    @Override
    public void updateEventoMovimentacao(
            final EventoMovimentacaoDTO eventoMovimentacao) {

        if (eventoMovimentacao == null) {
            throw new NullPointerException();
        }

        btnConfirmar.setEnabled(isAdicionar);
        fieldJustificativa.setEnabled(isAdicionar);

        idEventoMovimentacao = eventoMovimentacao.getId();
        movimentacao = eventoMovimentacao.getMovimentacao();
        funcionario = eventoMovimentacao.getFuncionario();
        tipo = eventoMovimentacao.getTipo();
        data = eventoMovimentacao.getData();

        fieldTipo.setText(eventoMovimentacao.getTipo().getTexto());
        fieldFuncionario.setText(eventoMovimentacao.getFuncionario().getNome());
        fieldData.setText(eventoMovimentacao.getData()
                .format(DateTimeFormatter.ISO_DATE));
        fieldJustificativa.setText(eventoMovimentacao.getJustificativa());

        revalidate();
        repaint();
    }

    @Override
    public void showError(final String message) {
        final String titulo = "Erro";
        final String messageCompleta = "Erro: " + message;
        JOptionPane.showMessageDialog(this, messageCompleta, titulo,
                JOptionPane.ERROR_MESSAGE);

    }

    @Override
    public void showSucesso() {
        final String titulo = "Sucesso";
        final String messageCompleta =
                isAdicionar ? "Evento de movimentação adicionado!"
                        : "Evento de movimentação alterado!";
        JOptionPane.showMessageDialog(this, messageCompleta, titulo,
                JOptionPane.INFORMATION_MESSAGE);

    }

    @Override
    public void showInfo(final String message) {
        final String titulo = "Informação";
        JOptionPane.showMessageDialog(this, message, titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
    }

    @Override
    public void close() {
        super.setVisible(false);
        super.dispose();
    }

    @Override
    public EventoMovimentacaoDTO getEventoMovimentacao() {
        final EventoMovimentacaoDTO eventoMovimentacao =
                new EventoMovimentacaoDTO();
        eventoMovimentacao.setId(idEventoMovimentacao);
        eventoMovimentacao.setFuncionario(funcionario);
        eventoMovimentacao.setTipo(tipo);
        eventoMovimentacao.setMovimentacao(movimentacao);
        eventoMovimentacao.setData(data);
        eventoMovimentacao.setJustificativa(fieldJustificativa.getText());
        return eventoMovimentacao;
    }

}
