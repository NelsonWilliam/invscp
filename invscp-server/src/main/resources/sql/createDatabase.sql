/* Script de criação do banco de dados do InvSCP */
/* Versão 1 */

/* Deleta as tabelas anteriores */

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
ALTER SCHEMA public OWNER TO "invscpAdmin";

/* Cria as novas tabelas */

CREATE TABLE localizacao (
    id serial PRIMARY KEY,
    nome character varying(255) NOT NULL,
    endereco character varying(255) NOT NULL,
    cep character varying(8) NOT NULL,
    cidade character varying(255) NOT NULL,
    uf character varying(2) NOT NULL
);

CREATE TABLE predio (
    id serial PRIMARY KEY,
    nome character varying(255) NOT NULL
);

CREATE TABLE sala (
    id serial PRIMARY KEY,
    nome character varying(255) NOT NULL,
    tipo character varying(255) NOT NULL
);

CREATE TABLE funcionario (
    id serial PRIMARY KEY,
    login character varying(255) UNIQUE NOT NULL,
    senha character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    cpf character varying(11) NOT NULL,
    email character varying(255) NOT NULL
);

CREATE TABLE departamento (
    id serial PRIMARY KEY,
    nome character varying(255) NOT NULL,
    de_patrimonio boolean NOT NULL
);

CREATE TABLE bem (
    id serial PRIMARY KEY,
    descricao character varying(255) NOT NULL,
    numero_tombamento bigint UNIQUE NOT NULL,
    data_cadastro date NOT NULL,
    data_aquisicao date NOT NULL,
    numero_nota_fiscal character varying(30) NOT NULL,
    especificacao character varying(255) NOT NULL,
    garantia date NOT NULL,
    marca character varying(255) NOT NULL,
    valor_compra numeric(12, 2) NOT NULL,
    situacao character varying(255) NOT NULL
);

CREATE TABLE ordem_servico (
    id serial PRIMARY KEY,
    data_cadastro date NOT NULL,
    data_conclusao date,
    valor numeric(12, 2) NOT NULL,
    situacao character varying(255) NOT NULL
);

CREATE TABLE baixa (
    id serial PRIMARY KEY,
    data date NOT NULL,
    motivo character varying(255) NOT NULL,
    observacoes character varying(255)
);

CREATE TABLE grupo_material (
    id serial PRIMARY KEY,
    nome character varying(255) NOT NULL,
    vida_util integer NOT NULL,
    depreciacao numeric(6, 5) NOT NULL
);

CREATE TABLE movimentacao ( 
   id serial PRIMARY KEY,
   etapa character varying(255) NOT NULL,
   num_guia_transporte character varying(32) UNIQUE
);

CREATE TABLE evento_movimentacao(
   id serial PRIMARY KEY,
   tipo character varying(255) NOT NULL,
   data date NOT NULL,
   justificativa character varying(255)
);

CREATE TABLE sistema(
  versao_db integer NOT NULL
);

/* Adiciona as chaves estrangeiras */

ALTER TABLE predio
    ADD COLUMN id_localizacao integer references localizacao(id);

ALTER TABLE sala
    ADD COLUMN id_predio integer references predio(id),
    ADD COLUMN id_departamento integer references departamento(id);
    
ALTER TABLE funcionario
    ADD COLUMN id_departamento integer references departamento(id);

ALTER TABLE departamento
    ADD COLUMN id_chefe integer references funcionario(id),
    ADD COLUMN id_chefe_substituto integer references funcionario(id);

ALTER TABLE ordem_servico
    ADD COLUMN id_funcionario integer references funcionario(id),
    ADD COLUMN id_bem integer references bem(id);

ALTER TABLE baixa
    ADD COLUMN id_funcionario integer references funcionario(id),
    ADD COLUMN id_bem integer references bem(id);

ALTER TABLE bem
    ADD COLUMN id_sala integer references sala(id),
    ADD COLUMN id_departamento integer references departamento(id),
    ADD COLUMN id_grupo_material integer references grupo_material(id);

ALTER TABLE movimentacao
    ADD COLUMN id_bem integer references bem(id),
    ADD COLUMN id_sala_origem integer references sala(id),
    ADD COLUMN id_sala_destino integer references sala(id);

ALTER TABLE evento_movimentacao
    ADD COLUMN id_movimentacao integer references movimentacao(id),
    ADD COLUMN id_funcionario integer references funcionario(id);

/* Adiciona os valores iniciais */

DO $$
DECLARE chefeId integer;
DECLARE departamentoId integer;
DECLARE localizacaoId integer;
DECLARE predioId integer;
DECLARE salaId integer;
BEGIN
    INSERT INTO funcionario (login, senha, nome, cpf, email)
        VALUES ('admin', 'admin', 'Administrador', '00000000000', 'admin@invscp.com') 
        RETURNING id INTO chefeId;

    INSERT INTO departamento (nome, de_patrimonio)
        VALUES ('Departamento de Patrimônio', TRUE)
        RETURNING id INTO departamentoId;

    INSERT INTO localizacao (nome, endereco, cep, cidade, uf)
        VALUES ('Localização do Depósito', 'Sem endereço', '00000000', 'Acrelândia', 'AC')
        RETURNING id INTO localizacaoId;

    INSERT INTO predio (nome)
        VALUES ('Prédio do Depósito')
        RETURNING id INTO predioId;

    INSERT INTO sala (nome, tipo)
        VALUES ('Depósito', 'DEPOSITO')
        RETURNING id INTO salaId;

    UPDATE predio
        SET id_localizacao = localizacaoId
        WHERE id = predioId;

    UPDATE sala
        SET id_predio = predioId, id_departamento = departamentoId
        WHERE id = salaId;

    UPDATE funcionario
        SET id_departamento = departamentoId
        WHERE id = chefeId;

    UPDATE departamento
        SET id_chefe = chefeId
        WHERE id = departamentoId;
END $$;

INSERT INTO grupo_material (nome, vida_util, depreciacao) VALUES ('Sem depreciação', -1, 0);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Aeronaves e embarcações', 25, 0.05);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Aparelhos de medição, equipamentos de comunicação e ferramentas', 10, 0.1);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Máquinas e equipamentos industriais', 10, 0.1);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Máquinas e equipamentos de produção de energia', 10, 0.1);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Computadores e equipamentos de informática', 5, 0.2);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Tratores leves, pesados e máquinas agrícolas', 10, 0.2);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Móveis', 10, 0.1);

INSERT INTO grupo_material (nome, vida_util, depreciacao)
	VALUES ('Veículos leves e pesados em geral', 15, 0.2);

INSERT INTO sistema (versao_db)
    VALUES (1);

/* Adiciona as restrições de not-null que não existiam antes para permitir a inserção dos valores iniciais */

ALTER TABLE predio
    ALTER COLUMN id_localizacao SET NOT NULL;

ALTER TABLE sala
    ALTER COLUMN id_predio SET NOT NULL,
    ALTER COLUMN id_departamento SET NOT NULL;

ALTER TABLE ordem_servico
    ALTER COLUMN id_funcionario SET NOT NULL,    
    ALTER COLUMN id_bem SET NOT NULL;

ALTER TABLE baixa
    ALTER COLUMN id_funcionario SET NOT NULL,    
    ALTER COLUMN id_bem SET NOT NULL;

ALTER TABLE bem
    ALTER COLUMN id_sala SET NOT NULL,    
    ALTER COLUMN id_departamento SET NOT NULL,
    ALTER COLUMN id_grupo_material SET NOT NULL;

ALTER TABLE movimentacao
    ALTER COLUMN id_bem SET NOT NULL, 
    ALTER COLUMN id_sala_origem SET NOT NULL, 
    ALTER COLUMN id_sala_destino SET NOT NULL;

ALTER TABLE evento_movimentacao
    ALTER COLUMN id_movimentacao SET NOT NULL, 
    ALTER COLUMN id_funcionario SET NOT NULL;
