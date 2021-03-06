package com.github.nelsonwilliam.invscp.server.model.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.nelsonwilliam.invscp.server.model.Baixa;
import com.github.nelsonwilliam.invscp.server.model.Bem;
import com.github.nelsonwilliam.invscp.server.util.DatabaseConnection;
import com.github.nelsonwilliam.invscp.shared.model.enums.MotivoBaixaEnum;

public class BaixaRepository implements Repository<Baixa> {

    @Override
    public List<Baixa> getAll() {
        final Connection connection = DatabaseConnection.getConnection();
        final List<Baixa> baixas = new ArrayList<Baixa>();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data,motivo,observacoes,id_funcionario,id_bem FROM baixa ORDER BY data ASC");
            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final LocalDate data = ((Date) r.getObject("data"))
                        .toLocalDate();
                final String motivo = (String) r.getObject("motivo");
                final String observacoes = (String) r.getObject("observacoes");
                final Integer idBem = (Integer) r.getObject("id_bem");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");

                final Baixa baixa = new Baixa();
                baixa.setId(id);
                baixa.setData(data);
                baixa.setMotivo(MotivoBaixaEnum.valueOf(motivo));
                baixa.setObservacoes(observacoes);
                baixa.setIdBem(idBem);
                baixa.setIdFuncionario(idFuncionario);
                baixas.add(baixa);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return baixas;
    }

    @Override
    public Baixa getById(final Integer id) {
        final Connection connection = DatabaseConnection.getConnection();
        Baixa baixa = null;
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data,motivo,observacoes,id_funcionario,id_bem FROM baixa WHERE id=?");
            s.setObject(1, id, Types.INTEGER);

            final ResultSet r = s.executeQuery();
            if (r.next()) {
                final LocalDate data = ((Date) r.getObject("data"))
                        .toLocalDate();
                final String motivo = (String) r.getObject("motivo");
                final String observacoes = (String) r.getObject("observacoes");
                final Integer idBem = (Integer) r.getObject("id_bem");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");

                baixa = new Baixa();
                baixa.setId(id);
                baixa.setData(data);
                baixa.setMotivo(MotivoBaixaEnum.valueOf(motivo));
                baixa.setObservacoes(observacoes);
                baixa.setIdBem(idBem);
                baixa.setIdFuncionario(idFuncionario);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return baixa;
    }

    public Baixa getByIdBem(final Integer idBem) {
        final Connection connection = DatabaseConnection.getConnection();
        Baixa baixa = null;
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data,motivo,observacoes,id_funcionario,id_bem FROM baixa WHERE id_bem=? LIMIT 1");
            s.setObject(1, idBem, Types.INTEGER);

            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final LocalDate data = ((Date) r.getObject("data"))
                        .toLocalDate();
                final String motivo = (String) r.getObject("motivo");
                final String observacoes = (String) r.getObject("observacoes");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");

                baixa = new Baixa();
                baixa.setId(id);
                baixa.setData(data);
                baixa.setMotivo(MotivoBaixaEnum.valueOf(motivo));
                baixa.setObservacoes(observacoes);
                baixa.setIdBem(idBem);
                baixa.setIdFuncionario(idFuncionario);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return baixa;
    }

    @Override
    public boolean add(final Baixa item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement s;
            if (item.getId() == null) {
                s = connection.prepareStatement(
                        "INSERT INTO baixa(data,motivo,observacoes,id_funcionario,id_bem) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                s.setObject(1, item.getData(), Types.DATE);
                s.setObject(2, item.getMotivo(), Types.VARCHAR);
                s.setObject(3, item.getObservacoes(), Types.VARCHAR);
                s.setObject(4, item.getIdFuncionario(), Types.INTEGER);
                s.setObject(5, item.getIdBem(), Types.INTEGER);
                s.executeUpdate();
                // Atualiza o item adicionado com seu novo ID
                final ResultSet rs = s.getGeneratedKeys();
                if (rs.next()) {
                    final Integer id = rs.getInt(1);
                    item.setId(id);
                }

            } else {
                s = connection.prepareStatement(
                        "INSERT INTO baixa(id,data,motivo,observacoes,id_funcionario,id_bem) VALUES (?,?,?,?,?,?)");
                s.setObject(1, item.getId(), Types.INTEGER);
                s.setObject(2, item.getData(), Types.DATE);
                s.setObject(3, item.getMotivo(), Types.VARCHAR);
                s.setObject(4, item.getObservacoes(), Types.VARCHAR);
                s.setObject(5, item.getIdFuncionario(), Types.INTEGER);
                s.setObject(6, item.getIdBem(), Types.INTEGER);
                s.executeUpdate();
            }

            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean add(final Iterable<Baixa> items) {
        boolean added = false;
        for (final Baixa item : items) {
            added |= add(item);
        }
        return added;
    }

    @Override
    public boolean update(final Baixa item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            if (item.getId() == null) {
                return false;
            }
            PreparedStatement s;
            s = connection.prepareStatement(
                    "UPDATE baixa SET data=?, motivo=?, observacoes=?, id_funcionario=?, id_bem=? WHERE id=?");
            s.setObject(1, item.getData(), Types.DATE);
            s.setObject(2, item.getMotivo(), Types.VARCHAR);
            s.setObject(3, item.getObservacoes(), Types.VARCHAR);
            s.setObject(4, item.getIdFuncionario(), Types.INTEGER);
            s.setObject(5, item.getIdBem(), Types.INTEGER);
            s.setObject(6, item.getId(), Types.INTEGER);
            s.executeUpdate();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean update(final Iterable<Baixa> items) {
        boolean updated = false;
        for (final Baixa item : items) {
            updated |= update(item);
        }
        return updated;
    }

    @Override
    public boolean remove(final Baixa item) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            if (item.getId() == null) {
                return false;
            }
            PreparedStatement s;
            s = connection.prepareStatement("DELETE FROM baixa WHERE id=?");
            s.setObject(1, item.getId(), Types.INTEGER);
            s.executeUpdate();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(final Iterable<Baixa> items) {
        boolean removed = false;
        for (final Baixa item : items) {
            removed |= remove(item);
        }
        return removed;
    }

    public List<Baixa> getBaixasByBem(final Bem bem) {
        final Connection connection = DatabaseConnection.getConnection();
        final List<Baixa> baixas = new ArrayList<Baixa>();
        try {
            final PreparedStatement s = connection.prepareStatement(
                    "SELECT id,data,motivo,observacoes,id_funcionario,id_bem FROM baixa WHERE id_bem=?");
            s.setObject(1, bem.getId(), Types.INTEGER);
            final ResultSet r = s.executeQuery();
            while (r.next()) {
                final Integer id = (Integer) r.getObject("id");
                final LocalDate data = ((Date) r.getObject("data"))
                        .toLocalDate();
                final String motivo = (String) r.getObject("motivo");
                final String observacoes = (String) r.getObject("observacoes");
                final Integer idBem = (Integer) r.getObject("id_bem");
                final Integer idFuncionario = (Integer) r
                        .getObject("id_funcionario");

                final Baixa baixa = new Baixa();
                baixa.setId(id);
                baixa.setData(data);
                baixa.setMotivo(MotivoBaixaEnum.valueOf(motivo));
                baixa.setObservacoes(observacoes);
                baixa.setIdBem(idBem);
                baixa.setIdFuncionario(idFuncionario);
                baixas.add(baixa);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return baixas;
    }

    public boolean existsBemBaixado(Integer idBem) {
        final Connection connection = DatabaseConnection.getConnection();
        try {
            final PreparedStatement s = connection
                    .prepareStatement("SELECT id FROM baixa WHERE id_bem=?");
            s.setObject(1, idBem, Types.INTEGER);
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
