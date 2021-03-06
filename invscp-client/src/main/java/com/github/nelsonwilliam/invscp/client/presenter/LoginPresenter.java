package com.github.nelsonwilliam.invscp.client.presenter;

import java.awt.event.ActionEvent;

import com.github.nelsonwilliam.invscp.client.util.Client;
import com.github.nelsonwilliam.invscp.client.view.LoginView;
import com.github.nelsonwilliam.invscp.shared.model.dto.FuncionarioDTO;

public class LoginPresenter extends Presenter<LoginView> {

    private final MainPresenter mainPresenter;

    public LoginPresenter(final LoginView view,
            final MainPresenter mainPresenter) {
        super(view);
        this.mainPresenter = mainPresenter;
        setupViewListeners();
    }

    private void setupViewListeners() {
        view.addConfirmListener((final ActionEvent e) -> {
            onConfirmLogin();
        });
    }

    private void onConfirmLogin() {
        final String login = view.getLogin();
        final FuncionarioDTO funcionario = Client
                .requestGetFuncionarioByLogin(login);
        if (funcionario == null) {
            view.showLoginFailed("Login desconhecido.");
            return;
        }

        final String senha = view.getSenha();
        if (!senha.equals(funcionario.getSenha())) {
            view.showLoginFailed("Senha incorreta.");
            return;
        }

        view.close();
        mainPresenter.setIdUsuario(funcionario.getId());
    }

}
