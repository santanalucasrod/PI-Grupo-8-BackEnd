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

-- somente para quem não criou ainda
-- utilizado como configuração do env
-- DROP USER IF EXISTS "developer";
-- CREATE USER "developer" IDENTIFIED BY "sptech";
-- GRANT ALL PRIVILEGES on cafeteria.* TO "developer";
-- FLUSH PRIVILEGES;

-- =============================================
-- FUNCIONÁRIOS (8)
-- =============================================
INSERT INTO funcionario (nome, senha, email, gerente) VALUES
("Raika",		   SHA2("senha123", 256), "raika@gmail.com", 			  1),
('Ana Lima',       SHA2('senha123', 256), 'ana.lima@cafeteria.com',       1),
('Bruno Costa',    SHA2('senha123', 256), 'bruno.costa@cafeteria.com',    0),
('Carla Souza',    SHA2('senha123', 256), 'carla.souza@cafeteria.com',    0),
('Diego Martins',  SHA2('senha123', 256), 'diego.martins@cafeteria.com',  0),
('Eduarda Ferreira',SHA2('senha123', 256),'eduarda.ferreira@cafeteria.com',0),
('Felipe Rocha',   SHA2('senha123', 256), 'felipe.rocha@cafeteria.com',   0),
('Gabriela Nunes', SHA2('senha123', 256), 'gabriela.nunes@cafeteria.com', 1),
('Henrique Dias',  SHA2('senha123', 256), 'henrique.dias@cafeteria.com',  0);

-- =============================================
-- CATEGORIAS
-- =============================================
INSERT INTO categoria (nome) VALUES
('Bebidas Quentes'),
('Bebidas Frias'),
('Cafés em Grão'),
('Salgados'),
('Doces');

-- =============================================
-- INGREDIENTES
-- =============================================
INSERT INTO ingrediente (nome) VALUES
('Café Espresso'),
('Leite Integral'),
('Leite Desnatado'),
('Creme de Leite'),
('Chocolate em Pó'),
('Canela'),
('Açúcar'),
('Chá Verde'),
('Chá de Camomila'),
('Chá de Hortelã'),
('Gelo'),
('Calda de Caramelo'),
('Calda de Baunilha'),
('Chantilly'),
('Granola'),
('Farinha de Trigo'),
('Queijo Mussarela'),
('Presunto'),
('Frango Desfiado'),
('Requeijão'),
('Bacon'),
('Ovos'),
('Manteiga'),
('Açúcar de Confeiteiro'),
('Chocolate Meio Amargo'),
('Doce de Leite'),
('Coco Ralado'),
('Amendoim'),
('Café Arábica em Grão'),
('Café Robusta em Grão'),
('Café Blend em Grão'),
('Leite de Aveia'),
('Leite de Amêndoas'),
('Matcha em Pó');

-- =============================================
-- PRODUTOS
-- =============================================
-- Bebidas Quentes (cat 1)
INSERT INTO produto (nome, categoria_id, preco_unidade, descricao, path_ft) VALUES
('Espresso Simples',        1, 5.00,  'Café espresso curto e encorpado',                        'espresso_simples.jpg'),
('Espresso Duplo',          1, 7.50,  'Dose dupla de café espresso',                             'espresso_duplo.jpg'),
('Cappuccino',              1, 10.00, 'Espresso com leite vaporizado e espuma',                  'cappuccino.jpg'),
('Latte',                   1, 11.00, 'Espresso com bastante leite vaporizado',                  'latte.jpg'),
('Mocha',                   1, 13.00, 'Espresso com chocolate e leite vaporizado',               'mocha.jpg'),
('Macchiato',               1, 9.00,  'Espresso marcado com espuma de leite',                    'macchiato.jpg'),
('Americano',               1, 7.00,  'Espresso diluído em água quente',                         'americano.jpg'),
('Chocolate Quente',        1, 11.00, 'Chocolate cremoso com leite integral',                    'chocolate_quente.jpg'),
('Chá Verde',               1, 7.00,  'Chá verde selecionado servido quente',                    'cha_verde.jpg'),
('Chá de Camomila',         1, 7.00,  'Chá calmante de camomila',                               'cha_camomila.jpg'),
('Chá de Hortelã',          1, 7.00,  'Chá refrescante de hortelã',                              'cha_hortela.jpg'),
('Latte de Matcha',         1, 14.00, 'Matcha japonês com leite vaporizado',                     'latte_matcha.jpg'),
('Café com Leite',          1, 8.00,  'Café coado com leite quente',                             'cafe_leite.jpg'),
('Cappuccino Caramelado',   1, 12.00, 'Cappuccino com calda de caramelo',                        'cappuccino_caramelo.jpg'),
('Latte Baunilha',          1, 12.00, 'Latte com calda de baunilha',                             'latte_baunilha.jpg');

-- Bebidas Frias (cat 2)
INSERT INTO produto (nome, categoria_id, preco_unidade, descricao, path_ft) VALUES
('Cold Brew',               2, 13.00, 'Café extraído a frio por 12h',                            'cold_brew.jpg'),
('Frappuccino',             2, 15.00, 'Café gelado batido com leite e gelo',                     'frappuccino.jpg'),
('Iced Latte',              2, 13.00, 'Latte servido sobre gelo',                                'iced_latte.jpg'),
('Iced Mocha',              2, 15.00, 'Mocha gelado com chantilly',                              'iced_mocha.jpg'),
('Smoothie de Café',        2, 16.00, 'Café batido com banana e leite de aveia',                 'smoothie_cafe.jpg'),
('Chocolate Gelado',        2, 12.00, 'Chocolate cremoso servido gelado',                        'chocolate_gelado.jpg'),
('Chá Verde Gelado',        2, 9.00,  'Chá verde com gelo e hortelã',                            'cha_verde_gelado.jpg'),
('Lemonade de Matcha',      2, 14.00, 'Matcha com limão e gelo',                                 'lemonade_matcha.jpg'),
('Caramel Frappuccino',     2, 16.00, 'Frappuccino com calda de caramelo e chantilly',           'caramel_frapp.jpg'),
('Iced Americano',          2, 10.00, 'Americano servido com bastante gelo',                     'iced_americano.jpg');

-- Cafés em Grão (cat 3)
INSERT INTO produto (nome, categoria_id, preco_unidade, descricao, path_ft) VALUES
('Café Arábica 250g',       3, 35.00, 'Grãos 100% Arábica torra média',                          'arabica_250.jpg'),
('Café Arábica 500g',       3, 65.00, 'Grãos 100% Arábica torra média pacote família',           'arabica_500.jpg'),
('Café Robusta 250g',       3, 28.00, 'Grãos Robusta torra escura, intenso',                     'robusta_250.jpg'),
('Blend Especial 250g',     3, 40.00, 'Blend exclusivo da casa, torra média-escura',              'blend_250.jpg'),
('Blend Especial 500g',     3, 75.00, 'Blend exclusivo da casa pacote família',                  'blend_500.jpg'),
('Café Geisha 100g',        3, 55.00, 'Café especial Geisha, notas florais e frutadas',          'geisha_100.jpg'),
('Café Natural 250g',       3, 38.00, 'Processo natural, adocicado e encorpado',                 'natural_250.jpg');

-- Salgados (cat 4)
INSERT INTO produto (nome, categoria_id, preco_unidade, descricao, path_ft) VALUES
('Croissant de Presunto',   4, 14.00, 'Croissant amanteigado recheado com presunto e queijo',    'croissant_presunto.jpg'),
('Croissant de Frango',     4, 15.00, 'Croissant com frango desfiado e requeijão',               'croissant_frango.jpg'),
('Pão de Queijo P',         4, 5.00,  'Mini pão de queijo quentinho',                            'pao_queijo_p.jpg'),
('Pão de Queijo G',         4, 8.00,  'Pão de queijo grande artesanal',                         'pao_queijo_g.jpg'),
('Quiche de Bacon',         4, 16.00, 'Quiche cremosa com bacon e queijo',                       'quiche_bacon.jpg'),
('Coxinha de Frango',       4, 9.00,  'Coxinha artesanal com frango e requeijão',                'coxinha.jpg'),
('Wrap de Frango',          4, 18.00, 'Wrap integral com frango grelhado e queijo',              'wrap_frango.jpg'),
('Sanduíche Natural',       4, 17.00, 'Pão integral com frango, requeijão e salada',             'sanduiche_natural.jpg');

-- Doces (cat 5)
INSERT INTO produto (nome, categoria_id, preco_unidade, descricao, path_ft) VALUES
('Brownie de Chocolate',    5, 12.00, 'Brownie fudgy com chocolate meio amargo',                 'brownie.jpg'),
('Cookie de Chocolate',     5, 9.00,  'Cookie crocante por fora e macio por dentro',             'cookie_choco.jpg'),
('Bolo de Cenoura',         5, 11.00, 'Fatia de bolo de cenoura com cobertura de chocolate',    'bolo_cenoura.jpg'),
('Cheesecake de Doce de Leite',5,16.00,'Cheesecake cremoso com doce de leite',                  'cheesecake_ddl.jpg'),
('Brigadeiro Gourmet',      5, 6.00,  'Brigadeiro artesanal com granulado belga',               'brigadeiro.jpg'),
('Fatia de Torta de Coco',  5, 13.00, 'Torta cremosa de coco com base de biscoito',             'torta_coco.jpg'),
('Muffin de Baunilha',      5, 10.00, 'Muffin fofinho com pepitas de chocolate',                'muffin_baunilha.jpg'),
('Pão de Mel',              5, 8.00,  'Pão de mel recheado com doce de leite e coberto de chocolate','pao_de_mel.jpg');

-- =============================================
-- PRODUTO_INGREDIENTE (associações)
-- =============================================
INSERT INTO produto_ingrediente (produto_id, ingrediente_id) VALUES
-- Espresso Simples (1)
(1, 1),
-- Espresso Duplo (2)
(2, 1),
-- Cappuccino (3)
(3, 1),(3, 2),(3, 14),
-- Latte (4)
(4, 1),(4, 2),
-- Mocha (5)
(5, 1),(5, 2),(5, 5),(5, 14),
-- Macchiato (6)
(6, 1),(6, 2),
-- Americano (7)
(7, 1),
-- Chocolate Quente (8)
(8, 2),(8, 5),(8, 7),
-- Chá Verde (9)
(9, 8),
-- Chá de Camomila (10)
(10, 9),
-- Chá de Hortelã (11)
(11, 10),
-- Latte de Matcha (12)
(12, 34),(12, 32),
-- Café com Leite (13)
(13, 1),(13, 2),
-- Cappuccino Caramelado (14)
(14, 1),(14, 2),(14, 12),
-- Latte Baunilha (15)
(15, 1),(15, 2),(15, 13),
-- Cold Brew (16)
(16, 1),(16, 11),
-- Frappuccino (17)
(17, 1),(17, 2),(17, 11),(17, 14),
-- Iced Latte (18)
(18, 1),(18, 2),(18, 11),
-- Iced Mocha (19)
(19, 1),(19, 2),(19, 5),(19, 11),(19, 14),
-- Smoothie de Café (20)
(20, 1),(20, 32),(20, 11),
-- Chocolate Gelado (21)
(21, 5),(21, 2),(21, 11),
-- Chá Verde Gelado (22)
(22, 8),(22, 11),(22, 10),
-- Lemonade de Matcha (23)
(23, 34),(23, 11),
-- Caramel Frappuccino (24)
(24, 1),(24, 2),(24, 12),(24, 11),(24, 14),
-- Iced Americano (25)
(25, 1),(25, 11),
-- Cafés em grão: ingredientes de grão
(26, 29),(27, 29),(28, 30),(29, 31),(30, 31),(31, 29),(32, 29),
-- Croissant de Presunto (33)
(33, 16),(33, 17),(33, 18),(33, 23),
-- Croissant de Frango (34)
(34, 16),(34, 19),(34, 20),(34, 23),
-- Pão de Queijo P (35)
(35, 17),(35, 22),
-- Pão de Queijo G (36)
(36, 17),(36, 22),
-- Quiche de Bacon (37)
(37, 16),(37, 17),(37, 21),(37, 22),(37, 4),
-- Coxinha de Frango (38)
(38, 16),(38, 19),(38, 20),
-- Wrap de Frango (39)
(39, 16),(39, 17),(39, 19),
-- Sanduíche Natural (40)
(40, 16),(40, 19),(40, 20),
-- Brownie de Chocolate (41)
(41, 25),(41, 16),(41, 23),(41, 7),(41, 22),
-- Cookie de Chocolate (42)
(42, 25),(42, 16),(42, 23),(42, 7),
-- Bolo de Cenoura (43)
(43, 16),(43, 7),(43, 22),(43, 5),(43, 23),
-- Cheesecake de Doce de Leite (44)
(44, 26),(44, 4),(44, 7),
-- Brigadeiro Gourmet (45)
(45, 26),(45, 2),(45, 5),(45, 23),
-- Fatia de Torta de Coco (46)
(46, 27),(46, 4),(46, 7),
-- Muffin de Baunilha (47)
(47, 13),(47, 16),(47, 22),(47, 7),(47, 23),(47, 25),
-- Pão de Mel (48)
(48, 26),(48, 25),(48, 16),(48, 7);

-- =============================================
-- PEDIDOS (20 pedidos)
-- =============================================
INSERT INTO pedido (dt_hr_pedido, dt_hr_pronto, valor_total, nome, funcionario_id) VALUES
('2024-06-01 08:05:00','2024-06-01 08:12:00', 21.00, 'Carlos Eduardo',  2),
('2024-06-01 08:30:00','2024-06-01 08:38:00', 35.00, 'Mariana Oliveira', 3),
('2024-06-01 09:00:00','2024-06-01 09:08:00', 27.50, 'Rodrigo Pimentel', 2),
('2024-06-01 09:15:00','2024-06-01 09:22:00', 14.00, 'Patrícia Freitas', 4),
('2024-06-01 10:00:00','2024-06-01 10:10:00', 46.00, 'Lucas Henrique',   5),
('2024-06-02 08:10:00','2024-06-02 08:20:00', 22.00, 'Fernanda Castro',  3),
('2024-06-02 09:30:00','2024-06-02 09:40:00', 33.00, 'Thiago Alves',     6),
('2024-06-02 10:45:00','2024-06-02 10:55:00', 28.00, 'Juliana Melo',     2),
('2024-06-03 08:00:00','2024-06-03 08:10:00', 55.00, 'André Vieira',     7),
('2024-06-03 09:00:00','2024-06-03 09:12:00', 19.00, 'Beatriz Santos',   4),
('2024-06-03 10:30:00','2024-06-03 10:42:00', 37.00, 'Rafael Cunha',     5),
('2024-06-04 08:20:00','2024-06-04 08:30:00', 24.00, 'Camila Torres',    3),
('2024-06-04 09:10:00','2024-06-04 09:18:00', 42.00, 'Gustavo Lima',     8),
('2024-06-04 10:00:00','2024-06-04 10:12:00', 31.00, 'Isabela Ramos',    6),
('2024-06-05 08:05:00','2024-06-05 08:15:00', 18.00, 'Pedro Monteiro',   2),
('2024-06-05 09:00:00','2024-06-05 09:10:00', 52.00, 'Aline Barbosa',    7),
('2024-06-05 10:20:00','2024-06-05 10:30:00', 29.00, 'Vitor Cardoso',    4),
('2024-06-06 08:00:00','2024-06-06 08:10:00', 44.00, 'Larissa Pinto',    5),
('2024-06-06 09:30:00','2024-06-06 09:40:00', 36.00, 'Marcos Teixeira',  3),
('2024-06-06 11:00:00','2024-06-06 11:12:00', 61.00, 'Renata Correia',   8);

-- =============================================
-- PEDIDO_STATUS
-- =============================================
INSERT INTO pedido_status (id_status, id_pedido) VALUES
(2,1),(2,2),(2,3),(2,4),(2,5),
(2,6),(3,7),(2,8),(2,9),(2,10),
(2,11),(2,12),(2,13),(3,14),(2,15),
(2,16),(1,17),(2,18),(1,19),(2,20);

-- =============================================
-- ITENS DE PEDIDO
-- =============================================
INSERT INTO itemPedido (produto_id, pedido_id, quantidade) VALUES
-- Pedido 1: Cappuccino + Brownie
(3,  1, 1),(41, 1, 1),
-- Pedido 2: Latte + Cookie + Croissant Presunto
(4,  2, 1),(42, 2, 1),(33, 2, 1),
-- Pedido 3: Mocha + Pão de Queijo G + Brigadeiro
(5,  3, 1),(36, 3, 1),(45, 3, 1),
-- Pedido 4: Espresso Duplo + Coxinha
(2,  4, 1),(38, 4, 1),
-- Pedido 5: Cold Brew + Wrap Frango + Cheesecake + Latte Matcha
(16, 5, 1),(39, 5, 1),(44, 5, 1),(12, 5, 1),
-- Pedido 6: Iced Latte + Muffin
(18, 6, 1),(47, 6, 1),
-- Pedido 7: Frappuccino + Bolo Cenoura + Pão de Queijo P
(17, 7, 1),(43, 7, 1),(35, 7, 2),
-- Pedido 8: Iced Mocha + Quiche Bacon
(19, 8, 1),(37, 8, 1),
-- Pedido 9: Caramel Frappuccino + Sanduíche Natural + Torta Coco + Café Arábica 250g
(24, 9, 1),(40, 9, 1),(46, 9, 1),(26, 9, 1),
-- Pedido 10: Americano + Pão de Mel
(7, 10, 1),(48, 10, 1),
-- Pedido 11: Latte Baunilha + Croissant Frango + Brownie
(15, 11, 1),(34, 11, 1),(41, 11, 1),
-- Pedido 12: Cappuccino Caramelado + Cookie
(14, 12, 1),(42, 12, 1),
-- Pedido 13: Smoothie Café + Quiche Bacon + Cheesecake + Blend Especial 250g
(20, 13, 1),(37, 13, 1),(44, 13, 1),(29, 13, 1),
-- Pedido 14: Lemonade Matcha + Pão de Queijo G + Brigadeiro
(23, 14, 1),(36, 14, 1),(45, 14, 2),
-- Pedido 15: Chá de Camomila + Muffin
(10, 15, 1),(47, 15, 1),
-- Pedido 16: Cold Brew + Iced Mocha + Wrap Frango + Bolo Cenoura + Café Geisha 100g
(16, 16, 1),(19, 16, 1),(39, 16, 1),(43, 16, 1),(31, 16, 1),
-- Pedido 17: Latte de Matcha + Croissant Presunto
(12, 17, 1),(33, 17, 1),
-- Pedido 18: Frappuccino + Caramel Frappuccino + Torta Coco + Sanduíche Natural
(17, 18, 1),(24, 18, 1),(46, 18, 1),(40, 18, 1),
-- Pedido 19: Espresso Simples + Pão de Queijo P + Brownie
(1, 19, 1),(35, 19, 2),(41, 19, 1),
-- Pedido 20: Mocha + Iced Latte + Croissant Frango + Cheesecake + Café Natural 250g + Pão de Mel
(5, 20, 1),(18, 20, 1),(34, 20, 1),(44, 20, 1),(32, 20, 1),(48, 20, 1);

-- =============================================
-- INFO_ADICIONAL
-- =============================================
INSERT INTO info_adicional (descricao, preferencia_individual, pedido_id) VALUES
('Sem açúcar',              'Espresso Duplo sem açúcar',           4),
('Leite vegetal',           'Latte com leite de aveia',            2),
('Extra quente',            'Cappuccino bem quente',                1),
('Sem lactose',             'Trocar leite integral por leite de amêndoas', 5),
('Chantilly extra',         'Mais chantilly no Iced Mocha',        8),
('Adoçante no lugar de açúcar', NULL,                              10),
('Grão moído na hora',      'Blend Especial moído fino',           13),
('Sem glúten - alergia',    'Verificar ingredientes do brownie',   11),
('Pouco gelo',              'Cold Brew com menos gelo',            16),
('Canela extra',            'Cappuccino Caramelado com mais canela',12);