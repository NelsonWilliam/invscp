package com.github.nelsonwilliam.invscp.shared.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.github.nelsonwilliam.invscp.shared.model.enums.OSSituacaoEnum;

public class OrdemServicoDTO implements DTO {

    private static final long serialVersionUID = 2820437419950550585L;

    private Integer id = null;

    private LocalDate dataCadastro = null;

    private LocalDate dataConclusao = null;

    private BigDecimal valor = null;

    private OSSituacaoEnum situacao = null;

    private FuncionarioDTO funcionario = null;

    private BemDTO bem = null;

    public final Integer getId() {
        return id;
    }

    public final void setId(final Integer id) {
        this.id = id;
    }

    public final LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public final void setDataCadastro(final LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public final LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public final void setDataConclusao(final LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public final BigDecimal getValor() {
        return valor;
    }

    public final void setValor(final BigDecimal valor) {
        this.valor = valor;
    }

    public final OSSituacaoEnum getSituacao() {
        return situacao;
    }

    public final void setSituacao(final OSSituacaoEnum situacao) {
        this.situacao = situacao;
    }

    public final FuncionarioDTO getFuncionario() {
        return funcionario;
    }

    public final void setFuncionario(final FuncionarioDTO funcionario) {
        this.funcionario = funcionario;
    }

    public final BemDTO getBem() {
        return bem;
    }

    public final void setBem(final BemDTO bem) {
        this.bem = bem;
    }

}
