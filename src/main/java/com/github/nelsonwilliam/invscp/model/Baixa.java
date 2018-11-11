package com.github.nelsonwilliam.invscp.model;

import java.time.LocalDate;

public class Baixa {

	private Integer id = null;

	private LocalDate data = null;

	private String motivo = null;

	private String observacoes = null;

	private Integer idFuncionario = null;

	private Integer idBem = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Integer getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public Integer getIdBem() {
		return idBem;
	}

	public void setIdBem(Integer idBem) {
		this.idBem = idBem;
	}

}