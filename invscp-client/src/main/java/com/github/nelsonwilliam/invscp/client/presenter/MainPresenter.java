package com.github.nelsonwilliam.invscp.client.presenter;

import java.awt.event.ActionEvent;

import com.github.nelsonwilliam.invscp.client.util.Client;
import com.github.nelsonwilliam.invscp.client.view.BensView;
import com.github.nelsonwilliam.invscp.client.view.DepartamentosView;
import com.github.nelsonwilliam.invscp.client.view.FuncionariosView;
import com.github.nelsonwilliam.invscp.client.view.LocalizacoesView;
import com.github.nelsonwilliam.invscp.client.view.LoginView;
import com.github.nelsonwilliam.invscp.client.view.MainView;
import com.github.nelsonwilliam.invscp.client.view.MenuView;
import com.github.nelsonwilliam.invscp.client.view.MovimentacoesView;
import com.github.nelsonwilliam.invscp.client.view.PrediosView;
import com.github.nelsonwilliam.invscp.client.view.SalasView;
import com.github.nelsonwilliam.invscp.client.view.ViewFactory;
import com.github.nelsonwilliam.invscp.shared.model.dto.FuncionarioDTO;

/**
 * Presenter responsável pela MainView e pela MenuView contida nela.
 */
public class MainPresenter extends Presenter<MainView> {

    private final MenuView menuView;

    private FuncionarioDTO usuario = null;

    public MainPresenter(final MainView mainView, final MenuView menuView) {
        super(mainView);
        this.menuView = menuView;
        setupViewListeners();
    }

    public void setIdUsuario(final Integer id) {
        usuario = id == null ? null : Client.requestGetFuncionarioById(id);
        menuView.updateUsuario(usuario);
        showNothing();
    }

    public Integer getIdUsuario() {
        return usuario == null ? null : usuario.getId();
    }

    public FuncionarioDTO getUsuario() {
        return usuario;
    }

    private void setupViewListeners() {
        menuView.addLoginListener((final ActionEvent e) -> {
            showLogin();
        });
        menuView.addLogoutListener((final ActionEvent e) -> {
            showLogout();
        });
        menuView.addDepartamentosListener((final ActionEvent e) -> {
            showDepartamentos();
        });
        menuView.addFuncionariosListener((final ActionEvent e) -> {
            showFuncionarios();
        });
        menuView.addLocalizacoesListener((final ActionEvent e) -> {
            showLocalizacoes();
        });
        menuView.addPrediosListener((final ActionEvent e) -> {
            showPredios();
        });
        menuView.addSalasListener((final ActionEvent e) -> {
            showSalas();
        });
        menuView.addBensListener((final ActionEvent e) -> {
            showBens();
        });
        menuView.addMovimentacoesListener((final ActionEvent e) -> {
            showMovimentacoes();
        });
    }

    private void showNothing() {
        view.updateSelectedView(null);
    }

    @SuppressWarnings("unused")
    private void showLogin() {
        final LoginView loginView = ViewFactory.createLogin(view);
        final LoginPresenter loginPresenter = new LoginPresenter(loginView,
                this);
        loginView.setVisible(true);
    }

    private void showLogout() {
        setIdUsuario(null);
        menuView.updateUsuario(null);
        showNothing();
    }

    @SuppressWarnings("unused")
    private void showDepartamentos() {
        // Apenas chefes podem manter departamentos
        if (getUsuario() == null || !getUsuario().getCargo().isChefe()) {
            showNothing();
            return;
        }

        final DepartamentosView deptView = ViewFactory
                .createDepartamentos(usuario);
        final DepartamentosPresenter deptPresenter = new DepartamentosPresenter(
                deptView, this);
        view.updateSelectedView(deptView);
    }

    @SuppressWarnings("unused")
    private void showFuncionarios() {
        // Apenas chefes podem manter departamentos
        if (getUsuario() == null || !getUsuario().getCargo().isChefe()) {
            showNothing();
            return;
        }

        final FuncionariosView funcView = ViewFactory.createFuncionarios();
        final FuncionariosPresenter funcPresenter = new FuncionariosPresenter(
                funcView, this);
        view.updateSelectedView(funcView);
    }

    @SuppressWarnings("unused")
    private void showLocalizacoes() {
        // Apenas chefes podem manter departamentos
        if (getUsuario() == null || !getUsuario().getCargo().isChefe()) {
            showNothing();
            return;
        }

        final LocalizacoesView locaView = ViewFactory.createLocalizacoes();
        final LocalizacoesPresenter locaPresenter = new LocalizacoesPresenter(
                locaView, this);
        view.updateSelectedView(locaView);
    }

    @SuppressWarnings("unused")
    private void showPredios() {
        // Apenas chefes podem manter departamentos
        if (getUsuario() == null || !getUsuario().getCargo().isChefe()) {
            showNothing();
            return;
        }

        final PrediosView predView = ViewFactory.createPredios();
        final PrediosPresenter predPresenter = new PrediosPresenter(predView,
                this);
        view.updateSelectedView(predView);
    }

    @SuppressWarnings("unused")
    private void showSalas() {
        // Apenas chefes podem manter salas
        if (getUsuario() == null || !getUsuario().getCargo().isChefe()) {
            showNothing();
            return;
        }

        final SalasView salasView = ViewFactory.createSalas();
        final SalasPresenter salasPresenter = new SalasPresenter(salasView,
                this);
        view.updateSelectedView(salasView);
    }

    @SuppressWarnings("unused")
    private void showBens() {
        final BensView bensView = ViewFactory.createBens(getUsuario());
        final BensPresenter bensPresenter = new BensPresenter(bensView, this);
        view.updateSelectedView(bensView);
    }

    @SuppressWarnings("unused")
    private void showMovimentacoes() {
        final MovimentacoesView movsView = ViewFactory
                .createMovimentacoes(getUsuario());
        final MovimentacoesPresenter movsPresenter = new MovimentacoesPresenter(
                movsView, this);
        view.updateSelectedView(movsView);
    }
}
