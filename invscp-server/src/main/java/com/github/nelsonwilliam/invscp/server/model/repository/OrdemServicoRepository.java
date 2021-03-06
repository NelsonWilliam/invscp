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
import com.github.nelsonwilliam.invscp.server.model.OrdemServico;
import com.github.nelsonwilliam.invscp.server.util.DatabaseConnection;
import com.github.nelsonwilliam.invscp.shared.model.enums.OSSituacaoEnum;

public class OrdemServicoRepository implements Repository<OrdemServico> {

    @Override
    public List<OrdemServico> getAll() {
        final Connection connection = DatabaseConnection.getConnection();
        final List<OrdemServico> ordens = new ArrayList<OrdemServico>();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data_cadastro,data_conclusao,valor,situacao,id_funcionario,id_bem FROM ordem_servico ORDER BY id");
            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataConclusao = r
                        .getObject("data_conclusao") == null ? null
                                : ((Date) r.getObject("data_conclusao"))
                                        .toLocalDate();
                final BigDecimal valor = (BigDecimal) r.getObject("valor");
                final String situacao = (String) r.getObject("situacao");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");
                final Integer idBem = (Integer) r.getObject("id_bem");

                final OrdemServico ordem = new OrdemServico();
                ordem.setId(id);
                ordem.setDataCadastro(dataCadastro);
                ordem.setDataConclusao(dataConclusao);
                ordem.setValor(valor);
                ordem.setSituacao(OSSituacaoEnum.valueOf(situacao));
                ordem.setIdFuncionario(idFuncionario);
                ordem.setIdBem(idBem);
                ordens.add(ordem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return ordens;

    }

    @Override
    public OrdemServico getById(final Integer id) {
        final Connection connection = DatabaseConnection.getConnection();
        OrdemServico ordem = null;
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data_cadastro,data_conclusao,valor,situacao,id_funcionario,id_bem FROM ordem_servico WHERE id=?");
            s.setObject(1, id, Types.INTEGER);

            final ResultSet r = s.executeQuery();
            if (r.next()) {
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataConclusao = r
                        .getObject("data_conclusao") == null ? null
                                : ((Date) r.getObject("data_conclusao"))
                                        .toLocalDate();
                final BigDecimal valor = (BigDecimal) r.getObject("valor");
                final String situacao = (String) r.getObject("situacao");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");
                final Integer idBem = (Integer) r.getObject("id_bem");

                ordem = new OrdemServico();
                ordem.setId(id);
                ordem.setDataCadastro(dataCadastro);
                ordem.setDataConclusao(dataConclusao);
                ordem.setValor(valor);
                ordem.setSituacao(OSSituacaoEnum.valueOf(situacao));
                ordem.setIdFuncionario(idFuncionario);
                ordem.setIdBem(idBem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return ordem;

    }

    public List<OrdemServico> getByIdBem(final Integer idBem) {
        final Connection connection = DatabaseConnection.getConnection();
        final List<OrdemServico> ordens = new ArrayList<OrdemServico>();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data_cadastro,data_conclusao,valor,situacao,id_funcionario,id_bem FROM ordem_servico WHERE id_bem=? ORDER BY id DESC");
            s.setObject(1, idBem, Types.INTEGER);

            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataConclusao = r
                        .getObject("data_conclusao") == null ? null
                                : ((Date) r.getObject("data_conclusao"))
                                        .toLocalDate();
                final BigDecimal valor = (BigDecimal) r.getObject("valor");
                final String situacao = (String) r.getObject("situacao");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");

                final OrdemServico ordem = new OrdemServico();
                ordem.setId(id);
                ordem.setDataCadastro(dataCadastro);
                ordem.setDataConclusao(dataConclusao);
                ordem.setValor(valor);
                ordem.setSituacao(OSSituacaoEnum.valueOf(situacao));
                ordem.setIdFuncionario(idFuncionario);
                ordem.setIdBem(idBem);
                ordens.add(ordem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return ordens;
    }

    @Override
    public boolean add(final OrdemServico item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement s;
            if (item.getId() == null) {
                s = connection.prepareStatement(
                        "INSERT INTO ordem_servico(data_cadastro,data_conclusao,valor,situacao,id_funcionario,id_bem)"
                                + " VALUES (?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                s.setObject(1, item.getDataCadastro(), Types.DATE);
                s.setObject(2, item.getDataConclusao(), Types.DATE);
                s.setObject(3, item.getValor(), Types.NUMERIC);
                s.setObject(4, item.getSituacao(), Types.VARCHAR);
                s.setObject(5, item.getIdFuncionario(), Types.INTEGER);
                s.setObject(6, item.getIdBem(), Types.INTEGER);
                s.executeUpdate();
                // Atualiza o item adicionado com seu novo ID
                final ResultSet rs = s.getGeneratedKeys();
                if (rs.next()) {
                    final Integer id = rs.getInt(1);
                    item.setId(id);
                }

            } else {
                s = connection.prepareStatement(
                        "INSERT INTO ordem_servico(id,data_cadastro,data_conclusao,valor,situacao,id_funcionario,id_bem)"
                                + " VALUES (?,?,?,?,?,?,?)");
                s.setObject(1, item.getId(), Types.INTEGER);
                s.setObject(2, item.getDataCadastro(), Types.DATE);
                s.setObject(3, item.getDataConclusao(), Types.DATE);
                s.setObject(4, item.getValor(), Types.NUMERIC);
                s.setObject(5, item.getSituacao(), Types.VARCHAR);
                s.setObject(6, item.getIdFuncionario(), Types.INTEGER);
                s.setObject(7, item.getIdBem(), Types.INTEGER);
                s.executeUpdate();
            }

            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean add(final Iterable<OrdemServico> items) {
        boolean added = false;
        for (final OrdemServico item : items) {
            added |= add(item);
        }
        return added;
    }

    @Override
    public boolean update(final OrdemServico item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            if (item.getId() == null) {
                return false;
            }
            PreparedStatement s;
            s = connection.prepareStatement(
                    "UPDATE ordem_servico SET data_cadastro=?, data_conclusao=?, valor=?, situacao=?,"
                            + " id_funcionario=?, id_bem=? WHERE id=?");
            s.setObject(1, item.getDataCadastro(), Types.DATE);
            s.setObject(2, item.getDataConclusao(), Types.DATE);
            s.setObject(3, item.getValor(), Types.NUMERIC);
            s.setObject(4, item.getSituacao(), Types.VARCHAR);
            s.setObject(5, item.getIdFuncionario(), Types.INTEGER);
            s.setObject(6, item.getIdBem(), Types.INTEGER);
            s.setObject(7, item.getId(), Types.INTEGER);
            s.executeUpdate();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(final Iterable<OrdemServico> items) {
        boolean updated = false;
        for (final OrdemServico item : items) {
            updated |= update(item);
        }
        return updated;
    }

    @Override
    public boolean remove(final OrdemServico item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            if (item.getId() == null) {
                return false;
            }
            PreparedStatement s;
            s = connection
                    .prepareStatement("DELETE FROM ordem_servico WHERE id=?");
            s.setObject(1, item.getId(), Types.INTEGER);
            s.executeUpdate();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(final Iterable<OrdemServico> items) {
        boolean removed = false;
        for (final OrdemServico item : items) {
            removed |= remove(item);
        }
        return removed;
    }

    public List<OrdemServico> getOsPendentesByBem(final Bem bem) {
        final Connection connection = DatabaseConnection.getConnection();
        final List<OrdemServico> ordens = new ArrayList<OrdemServico>();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data_cadastro,data_conclusao,valor,situacao,"
                            + "id_funcionario,id_bem FROM ordem_servico WHERE id_bem=? AND situacao=?");
            s.setObject(1, bem.getId(), Types.INTEGER);
            s.setObject(2, OSSituacaoEnum.PENDENTE.toString(), Types.VARCHAR);
            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final LocalDate dataCadastro = ((Date) r
                        .getObject("data_cadastro")).toLocalDate();
                final LocalDate dataConclusao = r
                        .getObject("data_conclusao") == null ? null
                                : ((Date) r.getObject("data_conclusao"))
                                        .toLocalDate();
                final BigDecimal valor = (BigDecimal) r.getObject("valor");
                final String situacao = (String) r.getObject("situacao");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");
                final Integer idBem = (Integer) r.getObject("id_bem");

                final OrdemServico ordem = new OrdemServico();
                ordem.setId(id);
                ordem.setDataCadastro(dataCadastro);
                ordem.setDataConclusao(dataConclusao);
                ordem.setValor(valor);
                ordem.setSituacao(OSSituacaoEnum.valueOf(situacao));
                ordem.setIdFuncionario(idFuncionario);
                ordem.setIdBem(idBem);
                ordens.add(ordem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return ordens;
    }

    public boolean existsBemPendente(Integer idBem) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id FROM ordem_servico WHERE id_bem=? AND situacao=?");
            s.setObject(1, idBem, Types.INTEGER);
            s.setObject(2, OSSituacaoEnum.PENDENTE.toString(), Types.VARCHAR);
            final ResultSet r = s.executeQuery();
            if (!r.next()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
