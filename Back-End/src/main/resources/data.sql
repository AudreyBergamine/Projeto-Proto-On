-- Password: password, há uma variável no codigo qeu armazena a senha em password
-- Aqui você deve substituir a senha criptografada correta
INSERT INTO USERS (username, password, role) VALUES ('admin', 'admin', 'ADMIN');









-- TRABALHO FRANCISCO =>>>>>>>>>>>
-- p2_proto_on.endereco definition

CREATE TABLE endereco (
  id_endereco int NOT NULL,
  bairro varchar(255) NOT NULL,
  cidade varchar(255) NOT NULL,
  complemento varchar(255) NOT NULL,
  estado varchar(255) NOT NULL,
  logradouro varchar(255) NOT NULL,
  nome_endereco varchar(255) DEFAULT NULL,
  num_cep varchar(255) NOT NULL,
  num_endereco varchar(255) NOT NULL,
  pais varchar(255) NOT NULL,
  tipo_endereco varchar(255) NOT NULL,
  PRIMARY KEY (id_endereco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.municipe definition

CREATE TABLE municipe (
  id int NOT NULL,
  email varchar(255) NOT NULL,
  nome varchar(255) NOT NULL,
  role enum('USER','ADMIN','MUNICIPE','FUNCIONARIO','COORDENADOR','SECRETARIO') DEFAULT NULL,
  senha varchar(255) NOT NULL,
  celular varchar(255) NOT NULL,
  data_nascimento date DEFAULT NULL,
  num_cpf varchar(255) NOT NULL,
  id_enderecofk int DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `UK_6on1pby0ob7xg5vyhg4xy1wtp` (email),
  UNIQUE KEY `UK_fjec4c77ls5w7gfkqe3dpnbgk` (num_cpf),
  UNIQUE KEY `UK_6xvxhoul88ytq9as4aknagoxd` (id_enderecofk),
  CONSTRAINT `FKg5k1bfx2xfvadfv6mkku9e2yv` FOREIGN KEY (id_enderecofk) REFERENCES endereco (id_endereco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.secretaria definition

CREATE TABLE secretaria (
  id_secretaria bigint NOT NULL AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  nome_responsavel varchar(255) DEFAULT NULL,
  nome_secretaria varchar(255) DEFAULT NULL,
  senha varchar(255) DEFAULT NULL,
  id_enderecofk int DEFAULT NULL,
  PRIMARY KEY (id_secretaria),
  KEY `FK1dqw4s06g4s3whj0s1lw7mobx` (id_enderecofk),
  CONSTRAINT `FK1dqw4s06g4s3whj0s1lw7mobx` FOREIGN KEY (id_enderecofk) REFERENCES endereco (id_endereco)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.assunto definition

CREATE TABLE assunto (
  id_assunto int NOT NULL AUTO_INCREMENT,
  assunto varchar(255) DEFAULT NULL,
  valor_protocolo double DEFAULT NULL,
  id_secretaria bigint DEFAULT NULL,
  PRIMARY KEY (id_assunto),
  KEY `FK9pk5rfeey2gvifq64j4if6l5n` (id_secretaria),
  CONSTRAINT `FK9pk5rfeey2gvifq64j4if6l5n` FOREIGN KEY (id_secretaria) REFERENCES secretaria (id_secretaria)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.funcionario definition

CREATE TABLE funcionario (
  id int NOT NULL,
  email varchar(255) NOT NULL,
  nome varchar(255) NOT NULL,
  role enum('USER','ADMIN','MUNICIPE','FUNCIONARIO','COORDENADOR','SECRETARIO') DEFAULT NULL,
  senha varchar(255) NOT NULL,
  celular varchar(255) NOT NULL,
  data_nascimento date NOT NULL,
  num_telefone_fixo varchar(255) DEFAULT NULL,
  num_cpf varchar(255) NOT NULL,
  id_enderecofk int DEFAULT NULL,
  id_secretariafk bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `UK_t45qja1wnv0hu1cdw6vqjljgy` (email),
  UNIQUE KEY `UK_owdwkyboteiq3oi2sg9bibokh` (num_cpf),
  UNIQUE KEY `UK_9sgns684uv8auu3vd1fqee536` (id_enderecofk),
  KEY `FKbsfs9h9bqp5ojgvqii2evj4wk` (id_secretariafk),
  CONSTRAINT `FKbsfs9h9bqp5ojgvqii2evj4wk` FOREIGN KEY (id_secretariafk) REFERENCES secretaria (id_secretaria),
  CONSTRAINT `FKrbdpesqtwse8jhuvgh7mf1v4g` FOREIGN KEY (id_enderecofk) REFERENCES endereco (id_endereco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.protocolo definition

CREATE TABLE protocolo (
  id_protocolo int NOT NULL AUTO_INCREMENT,
  assunto varchar(255) DEFAULT NULL,
  data_protocolo datetime(6) DEFAULT NULL,
  descricao text,
  numero_protocolo varchar(255) DEFAULT NULL,
  status tinyint DEFAULT NULL,
  valor double DEFAULT NULL,
  id_enderecofk int DEFAULT NULL,
  id_municipefk int DEFAULT NULL,
  id_secretariafk bigint DEFAULT NULL,
  PRIMARY KEY (id_protocolo),
  KEY `FK4j1gpwgwj0brhbbc021i38c8g` (id_enderecofk),
  KEY `FKtgw2440k8o1t90o4g2oswme9d` (id_municipefk),
  KEY `FKsbca7u5ttv2qjj3q21nccm12b` (id_secretariafk),
  CONSTRAINT `FK4j1gpwgwj0brhbbc021i38c8g` FOREIGN KEY (id_enderecofk) REFERENCES endereco (id_endereco),
  CONSTRAINT `FKsbca7u5ttv2qjj3q21nccm12b` FOREIGN KEY (id_secretariafk) REFERENCES secretaria (id_secretaria),
  CONSTRAINT `FKtgw2440k8o1t90o4g2oswme9d` FOREIGN KEY (id_municipefk) REFERENCES municipe (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.redirecionamento definition

CREATE TABLE redirecionamento (
  id int NOT NULL AUTO_INCREMENT,
  descricao varchar(255) DEFAULT NULL,
  dt_confirmacao datetime(6) DEFAULT NULL,
  dt_solicitacao datetime(6) DEFAULT NULL,
  nova_secretaria varchar(255) DEFAULT NULL,
  status_redirecionamento tinyint DEFAULT NULL,
  funcionario_id int NOT NULL,
  protocolo_id int NOT NULL,
  PRIMARY KEY (id),
  KEY `FKpd6dwxjlo8e0r5fcq4c02a6bs` (funcionario_id),
  KEY `FKbwyeh32j0p86idhukio9wvibp` (protocolo_id),
  CONSTRAINT `FKbwyeh32j0p86idhukio9wvibp` FOREIGN KEY (protocolo_id) REFERENCES protocolo (id_protocolo),
  CONSTRAINT `FKpd6dwxjlo8e0r5fcq4c02a6bs` FOREIGN KEY (funcionario_id) REFERENCES funcionario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- p2_proto_on.devolutiva definition

CREATE TABLE devolutiva (
  id_devolutiva int NOT NULL AUTO_INCREMENT,
  devolutiva text,
  momento_devolutiva datetime(6) DEFAULT NULL,
  id_funcionariofk int DEFAULT NULL,
  id_protocolofk int DEFAULT NULL,
  id_secretariafk bigint DEFAULT NULL,
  PRIMARY KEY (id_devolutiva),
  KEY `FKil85n8jsl9a3gfdvekotglhon` (id_funcionariofk),
  KEY `FKl66vo7pyw5p3r2symjjx2oqf5` (id_protocolofk),
  KEY `FKja51pl17blv8n1eh6udifnksc` (id_secretariafk),
  CONSTRAINT `FKil85n8jsl9a3gfdvekotglhon` FOREIGN KEY (id_funcionariofk) REFERENCES funcionario (id),
  CONSTRAINT `FKja51pl17blv8n1eh6udifnksc` FOREIGN KEY (id_secretariafk) REFERENCES secretaria (id_secretaria),
  CONSTRAINT `FKl66vo7pyw5p3r2symjjx2oqf5` FOREIGN KEY (id_protocolofk) REFERENCES protocolo (id_protocolo)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;