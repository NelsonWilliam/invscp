package com.github.nelsonwilliam.invscp.server.model;

import java.util.ArrayList;
import java.util.List;

import com.github.nelsonwilliam.invscp.server.model.repository.DepartamentoRepository;
import com.github.nelsonwilliam.invscp.server.model.repository.FuncionarioRepository;
import com.github.nelsonwilliam.invscp.server.model.repository.SalaRepository;
import com.github.nelsonwilliam.invscp.shared.exception.CRUDException;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalDeleteException;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalInsertException;
import com.github.nelsonwilliam.invscp.shared.exception.IllegalUpdateException;
import com.github.nelsonwilliam.invscp.shared.model.dto.DepartamentoDTO;
import com.github.nelsonwilliam.invscp.shared.model.dto.FuncionarioDTO;

public class Departamento implements Model<DepartamentoDTO> {

    private static final long serialVersionUID = -4732191299693035638L;

    private Integer id = null;

    private String nome = null;

    private Boolean dePatrimonio = null;;

    private Integer idChefe = null;

    private Integer idChefeSubstituto = null;

    @Override
    public void setValuesFromDTO(final DepartamentoDTO dto) {
        setId(dto.getId());
        setNome(dto.getNome());
        setDePatrimonio(dto.getDePatrimonio());
        if (dto.getChefe() != null) {
            setIdChefe(dto.getChefe().getId());
        }
        if (dto.getChefeSubstituto() != null) {
            setIdChefeSubstituto(dto.getChefeSubstituto().getId());
        }
    }

    @Override
    public DepartamentoDTO toDTO() {
        final DepartamentoDTO dto = new DepartamentoDTO();
        dto.setId(id);
        dto.setNome(nome);
        dto.setDePatrimonio(dePatrimonio);
        if (idChefe != null) {
            final FuncionarioRepository repo = new FuncionarioRepository();
            final Funcionario func = repo.getById(idChefe);
            // Para evitar loops infinitos ao criar os DTOs
            // departamentos/chefes, o DTO do chefe não tem o ID do seu
            // departamento.
            func.setIdDepartamento(null);
            dto.setChefe(func == null ? null : func.toDTO());
        }
        if (idChefeSubstituto != null) {
            final FuncionarioRepository repo = new FuncionarioRepository();
            final Funcionario func = repo.getById(idChefeSubstituto);
            // Para evitar loops infinitos ao criar os DTOs
            // departamentos/chefes, o DTO do chefe não tem o ID do seu
            // departamento.
            func.setIdDepartamento(null);
            dto.setChefeSubstituto(func == null ? null : func.toDTO());
        }
        return dto;
    }

    /**
     * Verifica se o elemento a ser deletado é válido, de acordo com as regras
     * de negócio. Caso seja válido, nada acontece. Caso não seja válido, é
     * lançada uma exeção com a mensagem de erro explicando o motivo. É
     * importante ressaltar que este método NÃO deleta o novo elemento, apenas
     * verifica a possibilidade de deletá-lo.
     *
     * @param usuario Funcionário autenticado que está tentando efetuar a
     *        operação.
     * @param idDept ID do departamento a ser deletado.
     * @throws IllegalDeleteException Se não for possível inserir o novo
     *         elemento.
     */
    public static void validarDeletar(final FuncionarioDTO usuario,
            final Integer idDept) throws IllegalDeleteException {

        // ---------------
        // IDENTIFICADORES
        // ---------------

        if (idDept == null) {
            throw new IllegalDeleteException(
                    "Não é possível remover o departamento sem informar o ID.");
        }

        final SalaRepository salaRepo = new SalaRepository();
        final FuncionarioRepository funcRepo = new FuncionarioRepository();
        final DepartamentoRepository deptRepo = new DepartamentoRepository();
        final Departamento dept = deptRepo.getById(idDept);

        if (dept == null) {
            throw new IllegalDeleteException(
                    "Não foi possível encontrar o departamento desejado.");
        }

        // ------------------
        // CONTROLE DE ACESSO
        // ------------------
        // Verificar controle de acesso (O usuário pode alterar esse tipo
        // de elemento, com esses atributos? Etc.).

        if (!usuario.getCargo().isChefeDePatrimonio()) {
            throw new IllegalDeleteException(
                    "Apenas chefes de patrimônio podem deletar departamentos.");
        }

        // -----------------
        // VALIDADE DE DADOS
        // -----------------
        // Verificar validade dos dados (Os novos atributos do elemento são
        // válidos? Há itens que não podem ser modificados? Há itens
        // repetidos/duplicados/já utilizados?Há itens obrigatórios faltando?
        // Etc).

        if (dept.getDePatrimonio()) {
            throw new IllegalDeleteException("O departamento de patrimônio ("
                    + dept.getNome() + ") não pode ser deletado.");
        }

        if (salaRepo.getByDepartamento(dept).size() > 0) {
            throw new IllegalDeleteException("O departamento " + dept.getNome()
                    + " não pode ser deletado pois existem salas pertecentes a ele.");
        }

        if (funcRepo.getByDepartamento(dept).size() > 0) {
            throw new IllegalDeleteException("O departamento " + dept.getNome()
                    + " não pode ser deletado pois existem funcionários pertecentes a ele.");
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
     * @param novoDept Departamento a ser inserido. Se o ID for nulo, um novo
     *        valor será gerado.
     * @throws IllegalInsertException Se não for possível inserir o novo
     *         elemento.
     */
    public static void validarInserir(final FuncionarioDTO usuario,
            final DepartamentoDTO novoDept) throws IllegalInsertException {

        // ---------------
        // IDENTIFICADORES
        // ---------------

        final DepartamentoRepository deptRepo = new DepartamentoRepository();

        if (novoDept.getId() != null) {
            final Departamento deptExistente = deptRepo
                    .getById(novoDept.getId());
            if (deptExistente != null) {
                throw new IllegalInsertException(
                        "Não é possível inserir a sala pois o ID já existe.");
            }
        }

        final Integer funcLogadoDeptId = usuario.getDepartamento() == null
                ? null
                : usuario.getDepartamento().getId();
        final boolean funcLogadoEraChefeDept = usuario.getCargo()
                .isChefeDeDepartamento();
        final boolean funcLogadoEraChefePatrimonio = usuario.getCargo()
                .isChefeDePatrimonio();

        // CONTROLE DE ACESSO
        if (novoDept.getId() == null && !funcLogadoEraChefePatrimonio) {
            throw new IllegalInsertException(
                    "Apenas chefes de patrimônio podem criar novos departamentos.");
        }

        if (novoDept.getId() != null
                && !(funcLogadoEraChefePatrimonio || (funcLogadoEraChefeDept
                        && funcLogadoDeptId.equals(novoDept.getId())))) {
            throw new IllegalInsertException(
                    "Apenas chefes de patrimônio e chefes do próprio departamento podem alterá-lo.");
        }

        try {
            validarCampos(novoDept);
        } catch (final CRUDException e) {
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
     * @param idAntigoDept ID do departamento a ser alterado. O ID não pode ser
     *        nulo, pois o departamento deve existir atualmente.
     * @param novoDept Departamento novo, com os novos dados desejados. O ID
     *        deve ser o mesmo do anterior.
     * @throws IllegalUpdateException Se não for possível inserir o novo
     *         elemento.
     */
    public static void validarAlterar(final FuncionarioDTO usuario,
            final Integer idAntigoDept, final DepartamentoDTO novoDept)
            throws IllegalUpdateException {

        // ---------------
        // IDENTIFICADORES
        // ---------------

        if (idAntigoDept == null) {
            throw new IllegalUpdateException(
                    "Não é possível alterar um departamento sem seu ID.");
        }

        final DepartamentoRepository deptRepo = new DepartamentoRepository();
        final Departamento antigoDept = deptRepo.getById(idAntigoDept);

        if (antigoDept == null) {
            throw new IllegalUpdateException(
                    "Não é possível alterar uma departamento inexistente.");
        }

        if (novoDept.getId() == null) {
            throw new IllegalUpdateException(
                    "Não é remover o ID do departamento.");
        }

        if (!antigoDept.getId().equals(novoDept.getId())) {
            throw new IllegalUpdateException(
                    "Não é possível alterar o ID do departamento.");
        }

        // Controle de Acesso
        if (!usuario.getCargo().isChefeDePatrimonio()) {
            if (!usuario.getCargo().isChefeDeDepartamento()) {
                throw new IllegalUpdateException(
                        "Você não tem permissão para alterar departamentos.");
            } else if (!usuario.getDepartamento().getId()
                    .equals(antigoDept.getId())) {
                throw new IllegalUpdateException(
                        "Você não tem permissão para alterar um departamento que não é o seu.");
            }
        }

        try {
            validarCampos(novoDept);
        } catch (final CRUDException e) {
            throw new IllegalUpdateException(e.getMessage());
        }

    }

    /**
     * Valida regras de negócio comuns tanto para inserção quanto para
     * alteração.
     */
    public static void validarCampos(final DepartamentoDTO dept)
            throws CRUDException {

        final DepartamentoRepository deptRepo = new DepartamentoRepository();

        if (dept.getNome() == null || dept.getNome().isEmpty()) {
            throw new CRUDException("O 'nome' é um campo obrigatório.");
        }
        if (dept.getDePatrimonio() == null) {
            throw new CRUDException(
                    "O 'de patrimônio' é um campo obrigatório.");
        }
        if (dept.getDePatrimonio()
                && !dept.getId().equals(deptRepo.getDePatrimonio().getId())) {
            throw new CRUDException(
                    "Só pode existir um departamento 'de patrimônio'.");
        }
        if (dept.getChefe() == null) {
            throw new CRUDException("O 'chefe' não pode ser vazio.");
        }
        if (dept.getChefe().getDepartamento() != null && !dept.getChefe()
                .getDepartamento().getId().equals(dept.getId())) {
            throw new CRUDException(
                    "O 'chefe' selecionado não pode pertencer a outro departamento.");
        }
        if (dept.getChefeSubstituto() != null && dept.getChefeSubstituto()
                .getId().equals(dept.getChefe().getId())) {
            throw new CRUDException(
                    "O 'chefe' não pode ser o mesmo que o 'chefe substituto'.");
        }

    }

    public static List<String> posAlterar(final FuncionarioDTO usuario,
            final DepartamentoDTO dept) {

        final List<String> messages = new ArrayList<String>();
        final FuncionarioRepository funcRepo = new FuncionarioRepository();

        // Se o novo chefe não possuia departamento, ele deve ser movido para o
        // departamento alterado.
        final Funcionario chefe = funcRepo.getById(dept.getChefe().getId());
        if (chefe.getIdDepartamento() == null) {
            chefe.setIdDepartamento(dept.getId());
            funcRepo.update(chefe);
            messages.add("O funcionário " + chefe.getNome()
                    + " foi movido ao departamento " + dept.getNome() + ".");
        }

        return messages;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getIdChefe() {
        return idChefe;
    }

    public void setIdChefe(final Integer idChefe) {
        this.idChefe = idChefe;
    }

    public Integer getIdChefeSubstituto() {
        return idChefeSubstituto;
    }

    public void setIdChefeSubstituto(final Integer idChefeSubstituto) {
        this.idChefeSubstituto = idChefeSubstituto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Boolean getDePatrimonio() {
        return dePatrimonio;
    }

    public void setDePatrimonio(final Boolean dePatrimonio) {
        this.dePatrimonio = dePatrimonio;
    }

}
