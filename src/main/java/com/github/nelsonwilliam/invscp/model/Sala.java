package com.github.nelsonwilliam.invscp.model;

import java.util.List;

import com.github.nelsonwilliam.invscp.exception.IllegalDeleteException;
import com.github.nelsonwilliam.invscp.exception.IllegalInsertException;
import com.github.nelsonwilliam.invscp.exception.IllegalUpdateException;
import com.github.nelsonwilliam.invscp.model.enums.TipoSalaEnum;
import com.github.nelsonwilliam.invscp.model.repository.DepartamentoRepository;
import com.github.nelsonwilliam.invscp.model.repository.PredioRepository;
import com.github.nelsonwilliam.invscp.model.repository.SalaRepository;

public class Sala implements Model {

    private static final long serialVersionUID = 509047893405447267L;

    private Integer id = null;

    private String nome = null;

    private TipoSalaEnum tipo = null;;

    private Integer idPredio = null;

    private Integer idDepartamento = null;

    /**
     * Verifica se o elemento a ser deletado é válido, de acordo com as regras
     * de negócio. Caso seja válido, nada acontece. Caso não seja válido, é
     * lançada uma exeção com a mensagem de erro explicando o motivo. É
     * importante ressaltar que este método NÃO deleta o novo elemento, apenas
     * verifica a possibilidade de deletá-lo.
     *
     * @param usuario Funcionário autenticado que está tentando efetuar a
     *        operação.
     * @param idSala ID da sala a ser deletada.
     * @throws IllegalDeleteException Se não for possível inserir o novo
     *         elemento.
     */
    public static void validarDeletar(final Funcionario usuario,
            final Integer idSala) throws IllegalDeleteException {

        // ---------------
        // IDENTIFICADORES
        // ---------------

        if (idSala == null) {
            throw new IllegalUpdateException(
                    "Não é possível remover salas sem informar o ID.");
        }

        final SalaRepository salaRepo = new SalaRepository();
        final Sala sala = salaRepo.getById(idSala);

        if (sala == null) {
            throw new IllegalUpdateException(
                    "Não foi possível encontrar a sala desejada.");
        }

        // ------------------
        // CONTROLE DE ACESSO
        // ------------------

        // TODO Verificar controle de acesso (O usuário pode deletar esse tipo
        // de elemento, com esses atributos? Etc.).

        // -----------------
        // VALIDADE DE DADOS
        // -----------------

        // TODO Verificar validade dos dados (Este item pode ser removido? É
        // um item obrigatório que não pode ser removido? Há itens dependento
        // dele? Etc.).

        // TODO Verificar se algum bem depende dessa sala.

        if (sala.getTipo() == TipoSalaEnum.DEPOSITO) {
            throw new IllegalUpdateException(
                    "Não é possível remover a sala de depósito.");
        }

    }

    /**
     * Verifica se o elemento a ser inserido é válido, de acordo com as regras
     * de negócio. Caso seja válido, nada acontece. Caso não seja válido, é
     * lançada uma exeção com a mensagem de erro explicando o motivo. É
     * importante ressaltar que este método NÃO insere o novo elemento, apenas
     * verifica a possibilidade de inserí-lo.
     *
     * @param usuario Funcionário autenticado que está tentando efetuar a
     *        operação.
     * @param novaSala Sala a ser inserida. Se o ID for nulo, um novo valor será
     *        gerado.
     * @throws IllegalInsertException Se não for possível inserir o novo
     *         elemento.
     */
    public static void validarInserir(final Funcionario usuario,
            final Sala novaSala) throws IllegalInsertException {

        // ---------------
        // IDENTIFICADORES
        // ---------------

        final SalaRepository salaRepo = new SalaRepository();

        if (novaSala.getId() != null) {
            final Sala salaExistente = salaRepo.getById(novaSala.getId());
            if (salaExistente != null) {
                throw new IllegalUpdateException(
                        "Não é possível inserir a sala pois o ID já existente.");
            }
        }

        // ------------------
        // CONTROLE DE ACESSO
        // ------------------

        // TODO Verificar controle de acesso (O usuário pode inserir esse tipo
        // de elemento, com esses atributos? Etc.).

        // -----------------
        // VALIDADE DE DADOS
        // -----------------

        // TODO Verificar validade dos dados (Os atributos do elemento são
        // válidos? Há itens repetidos/duplicados/já utilizados? Há itens
        // obrigatórios faltando? Etc.).

        try {
            validarCampos(novaSala);
        } catch (final IllegalArgumentException e) {
            throw new IllegalInsertException(e.getMessage());
        }
    }

    /**
     * Verifica se determinado elemento pode ser atualizado para os novos
     * valores desejados, de acordo com as regras de negócio. Caso seja válido,
     * nada acontece. Caso não seja válido, é lançada uma exeção com a mensagem
     * de erro explicando o motivo. É importante ressaltar que este método NÃO
     * altera o elemento, apenas verifica a possibilidade de alterá-lo.
     *
     * @param usuario Funcionário autenticado que está tentando efetuar a
     *        operação.
     * @param idAntigaSala ID da sala a ser alterada. O ID não pode ser nulo,
     *        pois a sala deve existir atualmente.
     * @param novaSala Sala nova, com os novos dados desejados. O ID deve ser o
     *        mesmo do anterior.
     * @throws IllegalUpdateException Se não for possível inserir o novo
     *         elemento.
     */
    public static void validarAlterar(final Funcionario usuario,
            final Integer idAntigaSala, final Sala novaSala)
            throws IllegalUpdateException {

        // ---------------
        // IDENTIFICADORES
        // ---------------

        if (idAntigaSala == null) {
            throw new IllegalUpdateException(
                    "Não é possível alterar uma sala sem seu ID.");
        }

        final SalaRepository salaRepo = new SalaRepository();
        final Sala antigaSala = salaRepo.getById(idAntigaSala);

        if (antigaSala == null) {
            throw new IllegalUpdateException(
                    "Não é possível alterar uma sala inexistente.");
        }

        if (novaSala.getId() == null) {
            throw new IllegalUpdateException("Não é remover o ID da sala.");
        }

        if (!antigaSala.getId().equals(novaSala.getId())) {
            throw new IllegalUpdateException(
                    "Não é possível alterar o ID da sala.");
        }

        // ------------------
        // CONTROLE DE ACESSO
        // ------------------

        // TODO Verificar controle de acesso (O usuário pode alterar esse tipo
        // de elemento, com esses atributos? Etc.).

        // -----------------
        // VALIDADE DE DADOS
        // -----------------

        // TODO Verificar validade dos dados (Os novos atributos do elemento são
        // válidos? Há itens que não podem ser modificados? Há itens
        // repetidos/duplicados/já utilizados?
        // Há itens obrigatórios faltando? Etc.).

        try {
            validarCampos(novaSala);
        } catch (final IllegalArgumentException e) {
            throw new IllegalUpdateException(e.getMessage());
        }

        if (antigaSala != null && antigaSala.getTipo() == TipoSalaEnum.DEPOSITO
                && novaSala.getTipo() != TipoSalaEnum.DEPOSITO) {
            throw new IllegalUpdateException(
                    "Não é possível remover a sala de depósito.");
        }

    }

    /**
     * Valida regras de negócio comuns tanto para inserção quanto para
     * alteração.
     */
    private static void validarCampos(final Sala sala)
            throws IllegalArgumentException {

        final SalaRepository salaRepo = new SalaRepository();
        final DepartamentoRepository deptRepo = new DepartamentoRepository();

        if (sala.getNome() == null || sala.getNome().isEmpty()) {
            throw new IllegalArgumentException(
                    "O 'nome' é um campo obrigatório.");
        }

        if (sala.getIdPredio() == null) {
            throw new IllegalArgumentException(
                    "O 'prédio' é um campo obrigatório.");
        }

        if (sala.getPredio() == null) {
            throw new IllegalArgumentException(
                    "O 'prédio' selecionado não existe.");
        }

        if (sala.getIdDepartamento() == null) {
            throw new IllegalArgumentException(
                    "O 'departamento' é um campo obrigatório.");
        }

        if (sala.getDepartamento() == null) {
            throw new IllegalArgumentException(
                    "O 'departamento' selecionado não existe.");
        }

        if (sala.getTipo() == null) {
            throw new IllegalArgumentException(
                    "O 'tipo' é um campo obrigatório.");
        }

        if (sala.getTipo() == TipoSalaEnum.DEPOSITO && (sala.getId() == null
                || !sala.getId().equals(salaRepo.getDeDeposito().getId()))) {
            throw new IllegalArgumentException(
                    "Já existe outra sala de depósito.");
        }

        if (sala.getTipo() == TipoSalaEnum.DEPOSITO
                && (sala.getIdDepartamento() == null
                        || !sala.getIdDepartamento()
                                .equals(deptRepo.getDePatrimonio().getId()))) {
            throw new IllegalArgumentException(
                    "A sala de depósito deve pertencer ao departamento de patrimônio.");
        }

    }

    public Integer getIdPredio() {
        return idPredio;
    }

    public Predio getPredio() {
        final PredioRepository predioRepo = new PredioRepository();
        return predioRepo.getById(idPredio);
    }

    public void setIdPredio(final Integer idPredio) {
        this.idPredio = idPredio;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public Departamento getDepartamento() {
        final DepartamentoRepository deptRepo = new DepartamentoRepository();
        return deptRepo.getById(idDepartamento);
    }

    public void setIdDepartamento(final Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public TipoSalaEnum getTipo() {
        return tipo;
    }

    public String getTipoString() {
        return tipo.toString();
    }

    public void setTipo(final TipoSalaEnum tipo) {
        this.tipo = tipo;
    }

    public void setTipo(final String tipo) {
        try {
            this.tipo = TipoSalaEnum.valueOfTexto(tipo);
        } catch (final IllegalArgumentException e) {
            this.tipo = TipoSalaEnum.valueOf(tipo);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public List<Predio> getPossiveisPredios() {
        final PredioRepository predioRepo = new PredioRepository();
        return predioRepo.getAll();
    }

    public List<Departamento> getPossiveisDepartamentos() {
        final DepartamentoRepository deptRepo = new DepartamentoRepository();
        return deptRepo.getAll();
    }
}
