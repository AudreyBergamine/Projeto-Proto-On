CREATE DATABASE IF NOT EXISTS PROTOON;

USE PROTOON;

CREATE TABLE hibernate_sequence (
    sequence_name VARCHAR(10) PRIMARY KEY,
    next_val INT
);

CREATE TABLE endereco (
    id_endereco INT PRIMARY KEY AUTO_INCREMENT,
    num_cep VARCHAR(10) NOT NULL,
    celular VARCHAR(16) NOT NULL,
    tipo_endereco VARCHAR(20),
    logradouro VARCHAR(50) NOT NULL,
    nome_endereco VARCHAR(10) NOT NULL,
    num_endereco VARCHAR(10) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(30) NOT NULL,
    cidade VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    pais VARCHAR(30) NOT NULL

);

CREATE TABLE municipe (
    id_municipe INT PRIMARY KEY AUTO_INCREMENT,
    nome_municipe VARCHAR(50) NOT NULL,
    email VARCHAR(50),
    senha VARCHAR(255),
    num_RG VARCHAR(10),
    num_CPF VARCHAR(20),
    data_nascimento DATE,
    id_enderecoFK INT(10)
);





-- ******************** // TODO: CRIAR ENTIDADES E SEUS MÉTODOS **********************

CREATE TABLE secretaria (
    id_secretaria INT PRIMARY KEY AUTO_INCREMENT,
    nome_secretaria VARCHAR(30) NOT NULL,
    nome_responsavel VARCHAR(30) NOT NULL,
    email VARCHAR(50),
    senha VARCHAR(255),
    id_enderecoFK INT(10)
);

CREATE TABLE departamento (
    id_departamento INT PRIMARY KEY AUTO_INCREMENT,
    nome_departamento VARCHAR(50) NOT NULL,
    nome_responsavel VARCHAR(30) NOT NULL,
    email_departamento VARCHAR(50),
    senha VARCHAR(255),
    id_enderecoFK INT(10)
);

CREATE TABLE funcionario (
    id_funcionario INT PRIMARY KEY AUTO_INCREMENT,
    id_departamentoFK INT(10),
    id_secretariaFK INT(10),
    id_enderecoFK INT(10),
    nome_funcionario VARCHAR(30) NOT NULL,
    num_CPF VARCHAR(20),
    data_nascimento DATE,
    cargo_funcionario VARCHAR(30),
    email_departamento VARCHAR(50),
    senha VARCHAR(255),
    num_telefone_movel VARCHAR(20) NOT NULL,
    num_telefone_fixo VARCHAR(20)
);


CREATE TABLE empresa (
    id_empresa INT PRIMARY KEY AUTO_INCREMENT,
    nome_social VARCHAR(100) NOT NULL,
    nome_fantasia VARCHAR(100),
    num_CNPJ VARCHAR(50) NOT NULL,
    inscricao_municipal VARCHAR(50) NOT NULL,
    id_enderecoFK INT(10),
    email VARCHAR(50) NOT NULL,
    num_telefone_movel VARCHAR(20) NOT NULL,
    num_telefone_fixo VARCHAR(20)
);



CREATE TABLE protocolo (
    id_protocolo INT PRIMARY KEY AUTO_INCREMENT,
    id_secretariaFK INT(10),
    id_departamentoFK INT(10),
    id_municipeFK INT(10),
    id_empresaFK INT(10),
    id_enderecoFK INT(10),
    id_valorFK INT(10),
    descricao TEXT,
    assunto VARCHAR(255),
    data_reclamacao DATE,
    status VARCHAR(50), -- ("Aberto" "Andamento" "Ciência/Entrega" "Finalizado")    
    comprovante_protocolo BLOB
);

CREATE TABLE valor_protocolo (
    id_valor_reclamacao INT PRIMARY KEY AUTO_INCREMENT,
    tabela_valores DECIMAL(10, 2),
    id_protocoloFK INT(10)
);


ALTER TABLE secretaria
    ADD CONSTRAINT fk_secretaria_endereco FOREIGN KEY (id_enderecoFK) REFERENCES endereco(id_endereco);

ALTER TABLE departamento
    ADD CONSTRAINT fk_departamento_endereco FOREIGN KEY (id_enderecoFK) REFERENCES endereco(id_endereco);

ALTER TABLE funcionario
    ADD CONSTRAINT fk_funcionario_departamento FOREIGN KEY (id_departamentoFK) REFERENCES departamento(id_departamento),
    ADD CONSTRAINT fk_funcionario_secretaria FOREIGN KEY (id_secretariaFK) REFERENCES secretaria(id_secretaria),
    ADD CONSTRAINT fk_funcionario_endereco FOREIGN KEY (id_enderecoFK) REFERENCES endereco(id_endereco);

ALTER TABLE municipe
    ADD CONSTRAINT fk_municipe_endereco FOREIGN KEY (id_enderecoFK) REFERENCES endereco(id_endereco);

ALTER TABLE empresa
    ADD CONSTRAINT fk_empresa_endereco FOREIGN KEY (id_enderecoFK) REFERENCES endereco(id_endereco);

ALTER TABLE protocolo
    ADD CONSTRAINT fk_protocolo_secretaria FOREIGN KEY (id_secretariaFK) REFERENCES secretaria(id_secretaria),
    ADD CONSTRAINT fk_protocolo_departamento FOREIGN KEY (id_departamentoFK) REFERENCES departamento(id_departamento),
    ADD CONSTRAINT fk_protocolo_municipe FOREIGN KEY (id_municipeFK) REFERENCES municipe(id_municipe),
    ADD CONSTRAINT fk_protocolo_empresa FOREIGN KEY (id_empresaFK) REFERENCES empresa(id_empresa),
    ADD CONSTRAINT fk_protocolo_endereco FOREIGN KEY (id_enderecoFK) REFERENCES endereco(id_endereco),
    ADD CONSTRAINT fk_protocolo_valor FOREIGN KEY (id_valorFK) REFERENCES valor_protocolo(id_valor_reclamacao);

ALTER TABLE valor_protocolo
    ADD CONSTRAINT fk_valor_protocolo FOREIGN KEY (id_protocoloFK) REFERENCES protocolo(id_protocolo);
