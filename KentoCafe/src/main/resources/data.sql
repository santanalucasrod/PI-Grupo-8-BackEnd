CREATE SCHEMA IF NOT EXISTS `cafeteria` ;
USE `cafeteria` ;
-- Tabela de Funcionarios
CREATE TABLE IF NOT EXISTS `cafeteria`.`funcionario` (
  `id` INT NOT NULL auto_increment,
  `nome` VARCHAR(50) NOT NULL,
  `senha` VARCHAR(128) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `gerente` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  unique(`email`)
  );

-- Categoria Produtos
CREATE TABLE IF NOT EXISTS `cafeteria`.`categoria` (
  `id` INT NOT NULL auto_increment,
  `nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
  );

-- Tabela Produto
CREATE TABLE IF NOT EXISTS `cafeteria`.`produto` (
  `id` INT NOT NULL auto_increment,
  `nome` VARCHAR(45) NOT NULL,
  `categoria_id` int not null,
  `preco_unidade` DECIMAL(5,2) NULL,
  `descricao` VARCHAR(200) NULL,
  `path_ft` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  constraint foreign key(categoria_id) references categoria(id)
  );


-- Tabela de Ingredientes
CREATE TABLE IF NOT EXISTS `cafeteria`.`ingrediente` (
  `id` INT NOT NULL auto_increment,
  `nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
  );

-- Tabela de status possíveis
CREATE TABLE IF NOT EXISTS `cafeteria`.`status` (
	`id` INT NOT NULL auto_increment,
	`nome` VARCHAR(12) NOT NULL,
	PRIMARY KEY (`id`)
);

insert into status(nome) values("Em preparo"), ("Pronto"), ("Cancelado");

-- Tabela de Pedidos
CREATE TABLE IF NOT EXISTS `cafeteria`.`pedido` (
  `id` INT NOT NULL auto_increment,
  `dt_hr_pedido` DATETIME NOT NULL,
  `dt_hr_pronto` DATETIME NOT NULL,
  `valor_total` decimal(6,2),
  `nome` Varchar(45) not null,
  `funcionario_id` int not null,
  CONSTRAINT `fk_pedido_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES funcionario(`id`),
  PRIMARY KEY (`id`)
);

-- tabela de pedido_status
CREATE TABLE IF NOT EXISTS `cafeteria`.`pedido_status` (
  `id_status` INT NOT NULL,
  `id_pedido` INT NOT NULL,
  CONSTRAINT `fk_pedido_status` FOREIGN KEY (`id_pedido`) REFERENCES pedido(`id`),
  CONSTRAINT `fk_status_pedido` FOREIGN KEY (`id_status`) REFERENCES status(`id`)
);

-- Tabela de informações adicionais
create table if not exists `cafeteria`.`info_adicional` (
    `id` int primary key auto_increment,
    `descricao` varchar(60) not null,
    `preferencia_individual` varchar(50),
    `pedido_id` int not null,
	CONSTRAINT `fk_pedido_info_adicional` FOREIGN KEY (`pedido_id`) REFERENCES pedido(`id`)
);

--  Tabela de Venda
CREATE TABLE IF NOT EXISTS `cafeteria`.`itemPedido` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `pedido_id` INT NOT NULL,
  `quantidade` INT NOT NULL,
  UNIQUE (`produto_id`, `pedido_id`),
  CONSTRAINT `fk_produto_has_pedido_produto1` FOREIGN KEY (`produto_id`) REFERENCES produto(`id`),
  CONSTRAINT `fk_produto_has_pedido_pedido1` FOREIGN KEY (`pedido_id`) REFERENCES pedido(`id`)
  );

-- Produto com ingrediente
CREATE TABLE IF NOT EXISTS `cafeteria`.`produto_ingrediente` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `produto_id` INT NOT NULL,
  `ingrediente_id` INT NOT NULL,
  UNIQUE(`produto_id`, `ingrediente_id`),
  CONSTRAINT `fk_produto_has_ingrediente_produto1` FOREIGN KEY (`produto_id`) REFERENCES produto(`id`),
  CONSTRAINT `fk_produto_has_ingrediente_ingrediente1` FOREIGN KEY (`ingrediente_id`) REFERENCES ingrediente(`id`)
);

DROP USER IF EXISTS "developer";
CREATE USER "developer" IDENTIFIED BY "sptech";
GRANT ALL PRIVILEGES on cafeteria.* TO "developer";
FLUSH PRIVILEGES;

insert into funcionario(nome, senha, email, gerente)
values ("Raika", "senha123", "raika@gmail.com", 1);

INSERT INTO categoria (nome) VALUES
('Bebidas Quentes'),
('Bebidas Frias'),
('Doces'),
('Salgados');

INSERT INTO ingrediente (nome) VALUES
('Café'),
('Leite'),
('Açúcar'),
('Chocolate'),
('Chá Verde'),
('Farinha'),
('Ovos'),
('Queijo'),
('Presunto');

INSERT INTO produto (nome, categoria_id, preco_unidade, descricao, path_ft) VALUES
('Café Expresso', 1, 5.00, 'Café forte e encorpado', 'img/cafe_expresso.jpg'),
('Cappuccino', 1, 7.50, 'Café com leite vaporizado e espuma', 'img/cappuccino.jpg'),
('Chocolate Quente', 1, 8.00, 'Bebida quente de chocolate cremoso', 'img/chocolate_quente.jpg'),
('Suco de Laranja', 2, 6.00, 'Suco natural de laranja', 'img/suco_laranja.jpg'),
('Croissant', 4, 4.50, 'Croissant amanteigado', 'img/croissant.jpg'),
('Bolo de Chocolate', 3, 6.50, 'Fatia de bolo de chocolate', 'img/bolo_chocolate.jpg');

INSERT INTO produto_ingrediente (produto_id, ingrediente_id) VALUES
(1, 1),
(2, 1),
(2, 2),
(2, 3),
(3, 4),
(3, 2),
(4, 3),
(5, 6),
(5, 7),
(5, 8),
(6, 4),
(6, 6),
(6, 7);

