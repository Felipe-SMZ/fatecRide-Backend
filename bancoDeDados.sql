CREATE DATABASE fatec;
USE fatec;
#drop database fatec;
-- Tabela de tipos de usuário
CREATE TABLE tipo_usuario (
	id_tipo_usuario INT PRIMARY KEY AUTO_INCREMENT,
	tipo VARCHAR(25) NOT NULL UNIQUE
);
 
-- Tabela de gêneros
CREATE TABLE genero (
	id_genero INT PRIMARY KEY AUTO_INCREMENT,
	genero VARCHAR(25) NOT NULL UNIQUE
);
 
-- Tabela de usuários
CREATE TABLE usuarios (
	id_usuario INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(100) NOT NULL,
	sobrenome VARCHAR(100) NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	senha VARCHAR(255) NOT NULL,
	telefone VARCHAR(20),
	foto VARCHAR(255),
	id_tipo_usuario INT NOT NULL,
	id_genero INT NOT NULL DEFAULT 1,
	FOREIGN KEY (id_tipo_usuario) REFERENCES tipo_usuario(id_tipo_usuario),
	FOREIGN KEY (id_genero) REFERENCES genero(id_genero)
);
 
-- Tabela de veículos (1 por usuário)
CREATE TABLE veiculos (
	id_veiculo INT PRIMARY KEY AUTO_INCREMENT,
	id_usuario INT NOT NULL UNIQUE,
	modelo VARCHAR(50),
	marca VARCHAR(50),
	placa VARCHAR(20) UNIQUE NOT NULL,
	cor VARCHAR(30),
	ano INT CHECK (ano >= 1900),
	FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
 
-- Tabela de status das caronas
CREATE TABLE status_carona (
	id_status INT PRIMARY KEY AUTO_INCREMENT,
	status_nome VARCHAR(25) NOT NULL UNIQUE
);
 
-- Tabela de caronas
CREATE TABLE caronas (
	id_carona INT PRIMARY KEY AUTO_INCREMENT,
	id_motorista INT NOT NULL,
	origem VARCHAR(255) NOT NULL,
	longitude_origem double NOT NULL,
    latitude_origem double NOT NULL,
	destino VARCHAR(255) NOT NULL,
	longitude_destino double NOT NULL,
    latitude_destino double NOT NULL,
	data_hora DATETIME NOT NULL,
	vagas_disponiveis INT NOT NULL,
	id_status INT,
	FOREIGN KEY (id_motorista) REFERENCES usuarios(id_usuario),
	FOREIGN KEY (id_status) REFERENCES status_carona(id_status)
);

 
-- Tabela de status das solicitações
CREATE TABLE status_solicitacao (
	id_status INT PRIMARY KEY AUTO_INCREMENT,
	status_nome VARCHAR(25) NOT NULL UNIQUE
);
 
-- Tabela de solicitações de carona
CREATE TABLE solicitacoes (
	id_solicitacao INT PRIMARY KEY AUTO_INCREMENT,
	id_carona INT NOT NULL,
	id_passageiro INT NOT NULL,
	data_solicitacao DATETIME DEFAULT CURRENT_TIMESTAMP,
	origem VARCHAR(255) NOT NULL,
	longitude_origem double NOT NULL,
    latitude_origem double NOT NULL,
	destino VARCHAR(255) NOT NULL,
	longitude_destino double NOT NULL,
    latitude_destino double NOT NULL,
	id_status INT DEFAULT 1,
	FOREIGN KEY (id_carona) REFERENCES caronas(id_carona),
	FOREIGN KEY (id_passageiro) REFERENCES usuarios(id_usuario),
	FOREIGN KEY (id_status) REFERENCES status_solicitacao(id_status)
);
 
-- Tabela de avaliações entre usuários
CREATE TABLE avaliacoes (
	id_avaliacao INT PRIMARY KEY AUTO_INCREMENT,
	id_usuario_avaliador INT NOT NULL,
	id_usuario_avaliado INT NOT NULL,
	nota INT CHECK (nota >= 1 AND nota <= 5),
	comentario TEXT,
	data DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (id_usuario_avaliador) REFERENCES usuarios(id_usuario),
	FOREIGN KEY (id_usuario_avaliado) REFERENCES usuarios(id_usuario)
);
 
-- Tabela de endereços dos usuários
CREATE TABLE enderecos (
	id_endereco INT PRIMARY KEY AUTO_INCREMENT,
	id_usuario INT NOT NULL,
	logradouro VARCHAR(100),
	numero VARCHAR(10),
	bairro VARCHAR(50),
	cidade VARCHAR(50),
	estado VARCHAR(2),
	cep VARCHAR(10),
	FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
 
-- Inserções iniciais
INSERT INTO tipo_usuario (tipo) VALUES ('motorista'), ('passageiro'), ('ambos');
 
INSERT INTO genero (genero) VALUES ('masculino'), ('feminino'), ('outro'), ('prefiro não dizer');
 select * from genero;
INSERT INTO status_carona (status_nome) VALUES ('ativa'), ('concluída'), ('cancelada');
 
INSERT INTO status_solicitacao (status_nome) VALUES ('pendente'), ('aceita'), ('recusada'), ('cancelada');


select * from caronas;

select * from usuarios;

DESCRIBE caronas;



