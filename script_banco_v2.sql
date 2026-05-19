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

-- Tabela de informações adicionais
create table if not exists `cafeteria`.`info_adicional` (
    `id` int primary key auto_increment,
    `descricao` varchar(60) not null,
    `preferencia_individual` varchar(50)
);

-- Tabela de Pedidos
CREATE TABLE IF NOT EXISTS `cafeteria`.`pedido` (
  `id` INT NOT NULL auto_increment,
  `dt_hr_pedido` DATETIME NOT NULL,
  `dt_hr_pronto` DATETIME NOT NULL,
  `status` VARCHAR(11) NOT NULL,
  `nome` Varchar(45) not null,
  `funcionario_id` int not null,
  `info_adicional_id` int not null,
  CONSTRAINT `fk_pedido_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES funcionario(`id`),
  CONSTRAINT `fk_pedido_info_adicional` FOREIGN KEY (`info_adicional_id`) REFERENCES info_adicional(`id`),
  PRIMARY KEY (`id`)
);

--  Tabela de Venda
CREATE TABLE IF NOT EXISTS `cafeteria`.`venda` (
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

-- somente para quem não criou ainda
-- utilizado como configuração do env
-- DROP USER IF EXISTS "developer";
-- CREATE USER "developer" IDENTIFIED BY "sptech";
-- GRANT ALL PRIVILEGES on cafeteria.* TO "developer";
-- FLUSH PRIVILEGES;

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

INSERT INTO info_adicional (descricao) VALUES
('Sem açúcar'),
('Com gelo'),
('Extra quente'),
('Sem lactose'),
('Adicionar chantilly');

INSERT INTO pedido (dt_hr_pedido, dt_hr_pronto, status, nome, funcionario_id, info_adicional_id)
VALUES
('2026-05-12 10:00:00', '2026-05-12 10:10:00', 'Pronto', 'Gabriela', 1, 1),
('2026-05-12 11:30:00', '2026-05-12 11:40:00', 'Pronto', 'Carlos', 1, 2),
('2026-05-12 15:00:00', '2026-05-12 15:20:00', 'Em preparo', 'Mariana', 1, 3),
('2026-05-12 16:45:00', '2026-05-12 17:00:00', 'Pronto', 'João', 1, 4),
('2026-05-12 18:00:00', '2026-05-12 18:15:00', 'Pronto', 'Ana', 1, 5);

INSERT INTO venda (produto_id, pedido_id, quantidade) VALUES
(1, 1, 2), 
(4, 2, 1), 
(2, 3, 1), 
(5, 4, 2), 
(3, 5, 1); 

