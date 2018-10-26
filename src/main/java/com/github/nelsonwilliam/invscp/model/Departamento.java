package com.github.nelsonwilliam.invscp.model;

import java.util.List;

import com.github.nelsonwilliam.invscp.model.repository.FuncionarioRepository;

public class Departamento implements Model {

	private static final long serialVersionUID = -4732191299693035638L;

	private Integer id = null;

	private String nome = "Departamento";

	private boolean dePatrimonio = false;

	private Integer idChefe = null;

	private Integer idChefeSubstituto = null;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdChefe() {
		return idChefe;
	}

	public void setIdChefe(Integer idChefe) {
		this.idChefe = idChefe;
	}

	public Integer getIdChefeSubstituto() {
		return idChefeSubstituto;
	}

	public void setIdChefeSubstituto(Integer idChefeSubstituto) {
		this.idChefeSubstituto = idChefeSubstituto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getDePatrimonio() {
		return dePatrimonio;
	}

	public void setDePatrimonio(Boolean dePatrimonio) {
		this.dePatrimonio = dePatrimonio;
	}

	public Funcionario getChefe() {
		if (idChefe == null) {
			return null;
		}
		FuncionarioRepository funcRepo = new FuncionarioRepository();
		return funcRepo.getById(idChefe);
	}

	public Funcionario getChefeSubstituto() {
		if (idChefeSubstituto == null) {
			return null;
		}
		FuncionarioRepository funcRepo = new FuncionarioRepository();
		return funcRepo.getById(idChefeSubstituto);
	}

	public List<Funcionario> getFuncionarios() {
		FuncionarioRepository funcRepo = new FuncionarioRepository();
		return funcRepo.getByDepartamento(this);
	}

	public List<Funcionario> getFuncionariosExcetoChefes() {
		FuncionarioRepository funcRepo = new FuncionarioRepository();
		return funcRepo.getByDepartamentoExcetoChefes(this);
	}

}