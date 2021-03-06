package com.github.nelsonwilliam.invscp.server.model.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.nelsonwilliam.invscp.server.model.Bem;
import com.github.nelsonwilliam.invscp.server.util.DatabaseConnection;
import com.github.nelsonwilliam.invscp.shared.model.dto.FiltroBemDTO;
import com.github.nelsonwilliam.invscp.shared.model.enums.BemSituacaoEnum;

public class BemRepository implements Repository<Bem> {

    @Override
    public List<Bem> getAll() {
        final Connection connection = DatabaseConnection.getConnection();
        final List<Bem> bens = new ArrayList<Bem>();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,descricao,numero_tombamento,data_cadastro,data_aquisicao,"
                            + "numero_nota_fiscal,especificacao,garantia,marca,valor_compra,situacao,"
                            + "id_sala,id_departamento,id_grupo_material FROM bem");
            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final String descricao = (String) r.getObject("descricao");
                final Long numTombamento = (Long) r
                        .getObject("numero_tombamento");
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataAquisicao = ((Date) r
                        .getObject("data_aquisicao")).toLocalDate();
                final String numNotaFiscal = (String) r
                        .getObject("numero_nota_fiscal");
                final String especificacao = (String) r
                        .getObject("especificacao");
                final LocalDate garantia = ((Date) r.getObject("garantia"))
                        .toLocalDate();
                final String marca = (String) r.getObject("marca");
                final BigDecimal valorCompra = (BigDecimal) r
                        .getObject("valor_compra");
                final String situacao = (String) r.getObject("situacao");
                final Integer idSala = (Integer) r.getObject("id_sala");
                final Integer idDepartamento = (Integer) r
                        .getObject("id_departamento");
                final Integer idGrupoMaterial = (Integer) r
                        .getObject("id_grupo_material");

                final Bem bem = new Bem();
                bem.setId(id);
                bem.setDescricao(descricao);
                bem.setNumeroTombamento(numTombamento);
                bem.setDataAquisicao(dataAquisicao);
                bem.setDataCadastro(dataCadastro);
                bem.setNumeroNotaFiscal(numNotaFiscal);
                bem.setEspecificacao(especificacao);
                bem.setGarantia(garantia);
                bem.setMarca(marca);
                bem.setValorCompra(valorCompra);
                bem.setSituacao(BemSituacaoEnum.valueOf(situacao));
                bem.setIdSala(idSala);
                bem.setIdDepartamento(idDepartamento);
                bem.setIdGrupoMaterial(idGrupoMaterial);
                bens.add(bem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return bens;

    }

    public List<Bem> getAllFiltered(final FiltroBemDTO filtro) {
        final Connection connection = DatabaseConnection.getConnection();
        final List<Bem> bens = new ArrayList<Bem>();
        try {
            final StringBuilder sql = new StringBuilder(
                    "SELECT id,descricao,numero_tombamento,data_cadastro,data_aquisicao,"
                            + "numero_nota_fiscal,especificacao,garantia,marca,valor_compra,situacao,"
                            + "id_sala,id_departamento,id_grupo_material FROM bem");

            boolean addedWhere = false;
            if (filtro.getDescricao() != null
                    && !filtro.getDescricao().isEmpty()) {
                if (!addedWhere) {
                    sql.append(" WHERE");
                    addedWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" descricao ILIKE ?");
            }
            if (filtro.getDepartamento() != null) {
                if (!addedWhere) {
                    sql.append(" WHERE");
                    addedWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" id_departamento=?");
            }
            if (filtro.getNumeroTombamento() != null) {
                if (!addedWhere) {
                    sql.append(" WHERE");
                    addedWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" CAST(numero_tombamento AS VARCHAR) LIKE ?");
            }
            if (filtro.getSituacao() != null) {
                if (!addedWhere) {
                    sql.append(" WHERE");
                    addedWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" situacao=?");
            }

            final PreparedStatement s = connection
                    .prepareStatement(sql.toString());
            int index = 1;
            if (filtro.getDescricao() != null
                    && !filtro.getDescricao().isEmpty()) {
                s.setObject(index, "%" + filtro.getDescricao() + "%",
                        Types.VARCHAR);
                index++;
            }
            if (filtro.getDepartamento() != null) {
                s.setObject(index, filtro.getDepartamento().getId(),
                        Types.INTEGER);
                index++;
            }
            if (filtro.getNumeroTombamento() != null) {
                s.setObject(index, "%" + filtro.getNumeroTombamento() + "%",
                        Types.VARCHAR);
                index++;
            }
            if (filtro.getSituacao() != null) {
                s.setObject(index, filtro.getSituacao().toString(),
                        Types.VARCHAR);
                index++;
            }

            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final String descricao = (String) r.getObject("descricao");
                final Long numTombamento = (Long) r
                        .getObject("numero_tombamento");
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataAquisicao = ((Date) r
                        .getObject("data_aquisicao")).toLocalDate();
                final String numNotaFiscal = (String) r
                        .getObject("numero_nota_fiscal");
                final String especificacao = (String) r
                        .getObject("especificacao");
                final LocalDate garantia = ((Date) r.getObject("garantia"))
                        .toLocalDate();
                final String marca = (String) r.getObject("marca");
                final BigDecimal valorCompra = (BigDecimal) r
                        .getObject("valor_compra");
                final String situacao = (String) r.getObject("situacao");
                final Integer idSala = (Integer) r.getObject("id_sala");
                final Integer idDepartamento = (Integer) r
                        .getObject("id_departamento");
                final Integer idGrupoMaterial = (Integer) r
                        .getObject("id_grupo_material");

                final Bem bem = new Bem();
                bem.setId(id);
                bem.setDescricao(descricao);
                bem.setNumeroTombamento(numTombamento);
                bem.setDataAquisicao(dataAquisicao);
                bem.setDataCadastro(dataCadastro);
                bem.setNumeroNotaFiscal(numNotaFiscal);
                bem.setEspecificacao(especificacao);
                bem.setGarantia(garantia);
                bem.setMarca(marca);
                bem.setValorCompra(valorCompra);
                bem.setSituacao(BemSituacaoEnum.valueOf(situacao));
                bem.setIdSala(idSala);
                bem.setIdDepartamento(idDepartamento);
                bem.setIdGrupoMaterial(idGrupoMaterial);
                bens.add(bem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return bens;

    }

    public boolean existsNumTombamento(final Long numTombamento) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id FROM bem WHERE numero_tombamento=?");
            s.setObject(1, numTombamento, Types.BIGINT);
            final ResultSet r = s.executeQuery();
            if (!r.next()) {
                return false;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean exitsInSala(Integer idSala) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            final PreparedStatement s = connection
                    .prepareStatement("SELECT id FROM bem WHERE id_sala=?");
            s.setObject(1, idSala, Types.INTEGER);
            final ResultSet r = s.executeQuery();
            if (!r.next()) {
                return false;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    @Override
    public Bem getById(final Integer id) {
        final Connection connection = DatabaseConnection.getConnection();
        Bem bem = null;
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,descricao,numero_tombamento,data_cadastro,data_aquisicao,"
                            + "numero_nota_fiscal,especificacao,garantia,marca,valor_compra,situacao,"
                            + "id_sala,id_departamento,id_grupo_material FROM bem WHERE id=?");
            s.setObject(1, id, Types.INTEGER);

            final ResultSet r = s.executeQuery();
            if (r.next()) {
                final String descricao = (String) r.getObject("descricao");
                final Long numTombamento = (Long) r
                        .getObject("numero_tombamento");
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataAquisicao = ((Date) r
                        .getObject("data_aquisicao")).toLocalDate();
                final String numNotaFiscal = (String) r
                        .getObject("numero_nota_fiscal");
                final String especificacao = (String) r
                        .getObject("especificacao");
                final LocalDate garantia = ((Date) r.getObject("garantia"))
                        .toLocalDate();
                final String marca = (String) r.getObject("marca");
                final BigDecimal valorCompra = (BigDecimal) r
                        .getObject("valor_compra");
                final String situacao = (String) r.getObject("situacao");
                final Integer idSala = (Integer) r.getObject("id_sala");
                final Integer idDepartamento = (Integer) r
                        .getObject("id_departamento");
                final Integer idGrupoMaterial = (Integer) r
                        .getObject("id_grupo_material");

                bem = new Bem();
                bem.setId(id);
                bem.setDescricao(descricao);
                bem.setNumeroTombamento(numTombamento);
                bem.setDataAquisicao(dataAquisicao);
                bem.setDataCadastro(dataCadastro);
                bem.setNumeroNotaFiscal(numNotaFiscal);
                bem.setEspecificacao(especificacao);
                bem.setGarantia(garantia);
                bem.setMarca(marca);
                bem.setValorCompra(valorCompra);
                bem.setSituacao(BemSituacaoEnum.valueOf(situacao));
                bem.setIdSala(idSala);
                bem.setIdDepartamento(idDepartamento);
                bem.setIdGrupoMaterial(idGrupoMaterial);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return bem;

    }

    @Override
    public boolean add(final Bem item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement s;
            if (item.getId() == null) {
                s = connection.prepareStatement(
                        "INSERT INTO bem(descricao,numero_tombamento,data_cadastro,data_aquisicao,"
                                + "numero_nota_fiscal,especificacao,garantia,marca,valor_compra,situacao,"
                                + "id_sala,id_departamento,id_grupo_material) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                s.setObject(1, item.getDescricao(), Types.VARCHAR);
                s.setObject(2, item.getNumeroTombamento(), Types.INTEGER);
                s.setObject(3, item.getDataCadastro(), Types.DATE);
                s.setObject(4, item.getDataAquisicao(), Types.DATE);
                s.setObject(5, item.getNumeroNotaFiscal(), Types.VARCHAR);
                s.setObject(6, item.getEspecificacao(), Types.VARCHAR);
                s.setObject(7, item.getGarantia(), Types.DATE);
                s.setObject(8, item.getMarca(), Types.VARCHAR);
                s.setObject(9, item.getValorCompra(), Types.NUMERIC);
                s.setObject(10, item.getSituacao(), Types.VARCHAR);
                s.setObject(11, item.getIdSala(), Types.INTEGER);
                s.setObject(12, item.getIdDepartamento(), Types.INTEGER);
                s.setObject(13, item.getIdGrupoMaterial(), Types.INTEGER);
                s.executeUpdate();
                // Atualiza o item adicionado com seu novo ID
                final ResultSet rs = s.getGeneratedKeys();
                if (rs.next()) {
                    final Integer id = rs.getInt(1);
                    item.setId(id);
                }

            } else {
                s = connection.prepareStatement(
                        "INSERT INTO bem(id,descricao,numero_tombamento,data_cadastro,data_aquisicao,"
                                + "numero_nota_fiscal,especificacao,garantia,marca,valor_compra,situacao,"
                                + "id_sala,id_departamento) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                s.setObject(1, item.getId(), Types.VARCHAR);
                s.setObject(2, item.getDescricao(), Types.VARCHAR);
                s.setObject(3, item.getNumeroTombamento(), Types.INTEGER);
                s.setObject(4, item.getDataCadastro(), Types.DATE);
                s.setObject(5, item.getDataAquisicao(), Types.DATE);
                s.setObject(6, item.getNumeroNotaFiscal(), Types.VARCHAR);
                s.setObject(7, item.getEspecificacao(), Types.VARCHAR);
                s.setObject(8, item.getGarantia(), Types.DATE);
                s.setObject(9, item.getMarca(), Types.VARCHAR);
                s.setObject(10, item.getValorCompra(), Types.NUMERIC);
                s.setObject(11, item.getSituacao(), Types.VARCHAR);
                s.setObject(12, item.getIdSala(), Types.INTEGER);
                s.setObject(13, item.getIdDepartamento(), Types.INTEGER);
                s.setObject(14, item.getIdGrupoMaterial(), Types.INTEGER);
                s.executeUpdate();
            }

            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean add(final Iterable<Bem> items) {
        boolean added = false;
        for (final Bem item : items) {
            added |= add(item);
        }
        return added;
    }

    @Override
    public boolean update(final Bem item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            if (item.getId() == null) {
                return false;
            }
            PreparedStatement s;
            s = connection.prepareStatement(
                    "UPDATE bem SET descricao=?, numero_tombamento=?, data_cadastro=?, data_aquisicao=?,"
                            + "numero_nota_fiscal=?, especificacao=?, garantia=?, marca=?, valor_compra=?,"
                            + "situacao=?, id_sala=?, id_departamento=?, id_grupo_material=? WHERE id=?");
            s.setObject(1, item.getDescricao(), Types.VARCHAR);
            s.setObject(2, item.getNumeroTombamento(), Types.INTEGER);
            s.setObject(3, item.getDataCadastro(), Types.DATE);
            s.setObject(4, item.getDataAquisicao(), Types.DATE);
            s.setObject(5, item.getNumeroNotaFiscal(), Types.VARCHAR);
            s.setObject(6, item.getEspecificacao(), Types.VARCHAR);
            s.setObject(7, item.getGarantia(), Types.DATE);
            s.setObject(8, item.getMarca(), Types.VARCHAR);
            s.setObject(9, item.getValorCompra(), Types.NUMERIC);
            s.setObject(10, item.getSituacao(), Types.VARCHAR);
            s.setObject(11, item.getIdSala(), Types.INTEGER);
            s.setObject(12, item.getIdDepartamento(), Types.INTEGER);
            s.setObject(13, item.getIdGrupoMaterial(), Types.INTEGER);
            s.setObject(14, item.getId(), Types.INTEGER);
            s.executeUpdate();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(final Iterable<Bem> items) {
        boolean updated = false;
        for (final Bem item : items) {
            updated |= update(item);
        }
        return updated;
    }

    @Override
    public boolean remove(final Bem item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            if (item.getId() == null) {
                return false;
            }
            PreparedStatement s;
            s = connection.prepareStatement("DELETE FROM bem WHERE id=?");
            s.setObject(1, item.getId(), Types.INTEGER);
            s.executeUpdate();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(final Iterable<Bem> items) {
        boolean removed = false;
        for (final Bem item : items) {
            removed |= remove(item);
        }
        return removed;
    }

}
