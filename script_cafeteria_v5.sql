CREATE SCHEMA IF NOT EXISTS `cafeteria`;
USE `cafeteria`;

-- Tabela de funcionários
CREATE TABLE IF NOT EXISTS `funcionario` (
  `id`      BIGINT          NOT NULL AUTO_INCREMENT,
  `nome`    VARCHAR(50)  NOT NULL,
  `email`   VARCHAR(254) NOT NULL,
  `senha`   VARCHAR(128) NOT NULL,
  `gerente` TINYINT      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`email`)
);

-- Categorias de produto
CREATE TABLE IF NOT EXISTS `categoria` (
  `id`   BIGINT         NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

-- Produtos
CREATE TABLE IF NOT EXISTS `produto` (
  `id`            BIGINT           NOT NULL AUTO_INCREMENT,
  `nome`          VARCHAR(45)   NOT NULL,
  `preco_unidade` DECIMAL(5,2)  NULL,
  `descricao`     VARCHAR(200)  NULL,
  `path_ft`       VARCHAR(200)   NULL,
  `categoria_id`  BIGINT           NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_produto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria`(`id`)
);

-- Ingredientes
CREATE TABLE IF NOT EXISTS `ingrediente` (
  `id`   BIGINT         NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

-- Tabela de status
CREATE TABLE IF NOT EXISTS `status` (
  `id`   BIGINT         NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `status` (`nome`) VALUES ('Em preparo'), ('Pronto'), ('Cancelado');

-- Tabela de pedido
CREATE TABLE IF NOT EXISTS `pedido` (
  `id`             BIGINT          NOT NULL AUTO_INCREMENT,
  `nome_cliente`   VARCHAR(45)  NOT NULL,
  `dt_hr_pedido`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dt_hr_pronto`   DATETIME     NULL,         
  `valor_total`    DECIMAL(6,2) NULL,
  `status_id`      BIGINT          NOT NULL DEFAULT 1, 
  `funcionario_id` BIGINT          NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_pedido_status`      FOREIGN KEY (`status_id`)      REFERENCES `status`(`id`),
  CONSTRAINT `fk_pedido_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `funcionario`(`id`)
);

-- Tabela do produto contido em pedido
CREATE TABLE IF NOT EXISTS `item_pedido` (
  `id`            BIGINT          NOT NULL AUTO_INCREMENT,
  `quantidade`    INT          NOT NULL,
  `preco_unidade` DECIMAL(5,2) NOT NULL,
  `pedido_id`     BIGINT          NOT NULL,
  `produto_id`    BIGINT          NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`produto_id`, `pedido_id`),
  CONSTRAINT `fk_item_pedido`   FOREIGN KEY (`pedido_id`)  REFERENCES `pedido`(`id`),
  CONSTRAINT `fk_item_produto`  FOREIGN KEY (`produto_id`) REFERENCES `produto`(`id`)
);

-- Composição de produto por ingredientes
CREATE TABLE IF NOT EXISTS `produto_ingrediente` (
  `id`             BIGINT NOT NULL AUTO_INCREMENT,
  `produto_id`     BIGINT NOT NULL,
  `ingrediente_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_pi_produto`     FOREIGN KEY (`produto_id`)     REFERENCES `produto`(`id`),
  CONSTRAINT `fk_pi_ingrediente` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente`(`id`)
);

-- Catálogo de personalizações disponíveis
CREATE TABLE IF NOT EXISTS `personalizacao` (
  `id`       BIGINT          NOT NULL AUTO_INCREMENT,
  `nome`     VARCHAR(60)  NOT NULL,
  `tipo`     VARCHAR(30)  NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`nome`)
);

-- Vínculo entre item do pedido e as personalizações escolhidas
CREATE TABLE IF NOT EXISTS `item_pedido_personalizacao` (
  `id`               BIGINT  NOT NULL AUTO_INCREMENT,
  `item_pedido_id`    BIGINT  NOT NULL,
  `personalizacao_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`item_pedido_id`, `personalizacao_id`),
  CONSTRAINT `fk_ip_itemPedido`     FOREIGN KEY (`item_pedido_id`)     REFERENCES `item_pedido`(`id`),
  CONSTRAINT `fk_ip_personalizacao` FOREIGN KEY (`personalizacao_id`) REFERENCES `personalizacao`(`id`)
);

-- somente para quem não criou ainda
-- utilizado como configuração do env
-- DROP USER IF EXISTS "developer";
-- SET GLOBAL validate_password_policy = LOW;
-- CREATE USER "developer" IDENTIFIED BY "sptech@2026";
-- GRANT ALL PRIVILEGES on cafeteria.* TO "developer";
-- FLUSH PRIVILEGES;

INSERT INTO funcionario (nome, senha, email, gerente) VALUES
("Raika",		   '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', "raika@gmail.com", 			  1),
('Ana Lima',       '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'ana.lima@cafeteria.com',       1),
('Bruno Costa',    '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'bruno.costa@cafeteria.com',    0),
('Carla Souza',    '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'carla.souza@cafeteria.com',    0),
('Diego Martins',  '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'diego.martins@cafeteria.com',  0),
('Eduarda Ferreira','$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG','eduarda.ferreira@cafeteria.com',0),
('Felipe Rocha',   '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'felipe.rocha@cafeteria.com',   0),
('Gabriela Nunes', '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'gabriela.nunes@cafeteria.com', 1),
('Henrique Dias',  '$2a$10$CcdT4yBJ39B9KJbplgpXQeqdWl8WVXyLd7iwnfQP5.XMavMkvrsxG', 'henrique.dias@cafeteria.com',  0);


INSERT INTO categoria (nome) VALUES
('Bebidas Quentes'),
('Bebidas Frias'),
('Cafés em Grão'),
('Salgados'),
('Doces');

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

INSERT INTO pedido (dt_hr_pedido, valor_total, nome_cliente, funcionario_id) VALUES
('2024-06-01 08:05:00', 21.00, 'Carlos Eduardo',  2),
('2024-06-01 08:30:00', 35.00, 'Mariana Oliveira', 3),
('2024-06-01 09:00:00', 27.50, 'Rodrigo Pimentel', 2),
('2024-06-01 09:15:00', 14.00, 'Patrícia Freitas', 4),
('2024-06-01 10:00:00', 46.00, 'Lucas Henrique',   5),
('2024-06-02 08:10:00', 22.00, 'Fernanda Castro',  3),
('2024-06-02 09:30:00', 33.00, 'Thiago Alves',     6),
('2024-06-02 10:45:00', 28.00, 'Juliana Melo',     2),
('2024-06-03 08:00:00', 55.00, 'André Vieira',     7),
('2024-06-03 09:00:00', 19.00, 'Beatriz Santos',   4),
('2024-06-03 10:30:00', 37.00, 'Rafael Cunha',     5),
('2024-06-04 08:20:00', 24.00, 'Camila Torres',    3),
('2024-06-04 09:10:00', 42.00, 'Gustavo Lima',     8),
('2024-06-04 10:00:00', 31.00, 'Isabela Ramos',    6),
('2024-06-05 08:05:00', 18.00, 'Pedro Monteiro',   2),
('2024-06-05 09:00:00', 52.00, 'Aline Barbosa',    7),
('2024-06-05 10:20:00', 29.00, 'Vitor Cardoso',    4),
('2024-06-06 08:00:00', 44.00, 'Larissa Pinto',    5),
('2024-06-06 09:30:00', 36.00, 'Marcos Teixeira',  3),
('2024-06-06 11:00:00', 61.00, 'Renata Correia',   8);

INSERT INTO item_pedido (produto_id, pedido_id, quantidade, preco_unidade) VALUES
-- Pedido 1: Cappuccino (10.00) + Brownie (12.00)
(3,  1, 1, 10.00),(41, 1, 1, 12.00),

-- Pedido 2: Latte (11.00) + Cookie (9.00) + Croissant Presunto (14.00)
(4,  2, 1, 11.00),(42, 2, 1, 9.00),(33, 2, 1, 14.00),

-- Pedido 3: Mocha (13.00) + Pão de Queijo G (8.00) + Brigadeiro (6.00)
(5,  3, 1, 13.00),(36, 3, 1, 8.00),(45, 3, 1, 6.00),

-- Pedido 4: Espresso Duplo (7.50) + Coxinha (9.00)
(2,  4, 1, 7.50),(38, 4, 1, 9.00),

-- Pedido 5: Cold Brew (13.00) + Wrap Frango (18.00) + Cheesecake (16.00) + Latte Matcha (14.00)
(16, 5, 1, 13.00),(39, 5, 1, 18.00),(44, 5, 1, 16.00),(12, 5, 1, 14.00),

-- Pedido 6: Iced Latte (13.00) + Muffin (10.00)
(18, 6, 1, 13.00),(47, 6, 1, 10.00),

-- Pedido 7: Frappuccino (15.00) + Bolo Cenoura (11.00) + Pão de Queijo P (5.00)
(17, 7, 1, 15.00),(43, 7, 1, 11.00),(35, 7, 2, 5.00),

-- Pedido 8: Iced Mocha (15.00) + Quiche Bacon (16.00)
(19, 8, 1, 15.00),(37, 8, 1, 16.00),

-- Pedido 9: Caramel Frappuccino (16.00) + Sanduíche Natural (17.00) + Torta Coco (13.00) + Café Arábica 250g (35.00)
(24, 9, 1, 16.00),(40, 9, 1, 17.00),(46, 9, 1, 13.00),(26, 9, 1, 35.00),

-- Pedido 10: Americano (7.00) + Pão de Mel (8.00)
(7, 10, 1, 7.00),(48, 10, 1, 8.00),

-- Pedido 11: Latte Baunilha (12.00) + Croissant Frango (15.00) + Brownie (12.00)
(15, 11, 1, 12.00),(34, 11, 1, 15.00),(41, 11, 1, 12.00),

-- Pedido 12: Cappuccino Caramelado (12.00) + Cookie (9.00)
(14, 12, 1, 12.00),(42, 12, 1, 9.00),

-- Pedido 13: Smoothie Café (16.00) + Quiche Bacon (16.00) + Cheesecake (16.00) + Blend Especial 250g (40.00)
(20, 13, 1, 16.00),(37, 13, 1, 16.00),(44, 13, 1, 16.00),(29, 13, 1, 40.00),

-- Pedido 14: Lemonade Matcha (14.00) + Pão de Queijo G (8.00) + Brigadeiro (6.00)
(23, 14, 1, 14.00),(36, 14, 1, 8.00),(45, 14, 2, 6.00),

-- Pedido 15: Chá de Camomila (7.00) + Muffin (10.00)
(10, 15, 1, 7.00),(47, 15, 1, 10.00),

-- Pedido 16: Cold Brew (13.00) + Iced Mocha (15.00) + Wrap Frango (18.00) + Bolo Cenoura (11.00) + Café Geisha 100g (55.00)
(16, 16, 1, 13.00),(19, 16, 1, 15.00),(39, 16, 1, 18.00),(43, 16, 1, 11.00),(31, 16, 1, 55.00),

-- Pedido 17: Latte de Matcha (14.00) + Croissant Presunto (14.00)
(12, 17, 1, 14.00),(33, 17, 1, 14.00),

-- Pedido 18: Frappuccino (15.00) + Caramel Frappuccino (16.00) + Torta Coco (13.00) + Sanduíche Natural (17.00)
(17, 18, 1, 15.00),(24, 18, 1, 16.00),(46, 18, 1, 13.00),(40, 18, 1, 17.00),

-- Pedido 19: Espresso Simples (5.00) + Pão de Queijo P (5.00) + Brownie (12.00)
(1, 19, 1, 5.00),(35, 19, 2, 5.00),(41, 19, 1, 12.00),

-- Pedido 20: Mocha (13.00) + Iced Latte (13.00) + Croissant Frango (15.00) + Cheesecake (16.00) + Café Natural 250g (38.00) + Pão de Mel (8.00)
(5, 20, 1, 13.00),(18, 20, 1, 13.00),(34, 20, 1, 15.00),(44, 20, 1, 16.00),(32, 20, 1, 38.00),(48, 20, 1, 8.00);

INSERT INTO produto_ingrediente (produto_id, ingrediente_id) VALUES
-- 1: Espresso Simples (Café Espresso)
(1, 1),

-- 2: Espresso Duplo (Café Espresso)
(2, 1),

-- 3: Cappuccino (Café Espresso, Leite Integral, Chantilly)
(3, 1), (3, 2), (3, 14),

-- 4: Latte (Café Espresso, Leite Integral)
(4, 1), (4, 2),

-- 5: Mocha (Café Espresso, Leite Integral, Chocolate em Pó, Chantilly)
(5, 1), (5, 2), (5, 5), (5, 14),

-- 6: Macchiato (Café Espresso, Leite Integral)
(6, 1), (6, 2),

-- 7: Americano (Café Espresso)
(7, 1),

-- 8: Chocolate Quente (Leite Integral, Chocolate em Pó, Açúcar)
(8, 2), (8, 5), (8, 7),

-- 9: Chá Verde (Chá Verde)
(9, 8),

-- 10: Chá de Camomila (Chá de Camomila)
(10, 9),

-- 11: Chá de Hortelã (Chá de Hortelã)
(11, 10),

-- 12: Latte de Matcha (Matcha em Pó, Leite de Aveia)
(12, 34), (12, 32),

-- 13: Café com Leite (Café Espresso, Leite Integral)
(13, 1), (13, 2),

-- 14: Cappuccino Caramelado (Café Espresso, Leite Integral, Calda de Caramelo)
(14, 1), (14, 2), (14, 12),

-- 15: Latte Baunilha (Café Espresso, Leite Integral, Calda de Baunilha)
(15, 1), (15, 2), (15, 13),

-- 16: Cold Brew (Café Espresso, Gelo)
(16, 1), (16, 11),

-- 17: Frappuccino (Café Espresso, Leite Integral, Gelo, Chantilly)
(17, 1), (17, 2), (17, 11), (17, 14),

-- 18: Iced Latte (Café Espresso, Leite Integral, Gelo)
(18, 1), (18, 2), (18, 11),

-- 19: Iced Mocha (Café Espresso, Leite Integral, Chocolate em Pó, Gelo, Chantilly)
(19, 1), (19, 2), (19, 5), (19, 11), (19, 14),

-- 20: Smoothie de Café (Café Espresso, Leite de Aveia, Gelo)
(20, 1), (20, 32), (20, 11),

-- 21: Chocolate Gelado (Chocolate em Pó, Leite Integral, Gelo)
(21, 5), (21, 2), (21, 11),

-- 22: Chá Verde Gelado (Chá Verde, Gelo, Chá de Hortelã)
(22, 8), (22, 11), (22, 10),

-- 23: Lemonade de Matcha (Matcha em Pó, Gelo)
(23, 34), (23, 11),

-- 24: Caramel Frappuccino (Café Espresso, Leite Integral, Calda de Caramelo, Gelo, Chantilly)
(24, 1), (24, 2), (24, 12), (24, 11), (24, 14),

-- 25: Iced Americano (Café Espresso, Gelo)
(25, 1), (25, 11),

-- 26: Café Arábica 250g (Café Arábica em Grão)
(26, 29),

-- 27: Café Arábica 500g (Café Arábica em Grão)
(27, 29),

-- 28: Café Robusta 250g (Café Robusta em Grão)
(28, 30),

-- 29: Blend Especial 250g (Café Blend em Grão)
(29, 31),

-- 30: Blend Especial 500g (Café Blend em Grão)
(30, 31),

-- 31: Café Geisha 100g (Café Arábica em Grão)
(31, 29),

-- 32: Café Natural 250g (Café Arábica em Grão)
(32, 29),

-- 33: Croissant de Presunto (Farinha de Trigo, Queijo Mussarela, Presunto, Manteiga)
(33, 16), (33, 17), (33, 18), (33, 23),

-- 34: Croissant de Frango (Farinha de Trigo, Frango Desfiado, Requeijão, Manteiga)
(34, 16), (34, 19), (34, 20), (34, 23),

-- 35: Pão de Queijo P (Queijo Mussarela, Ovos)
(35, 17), (35, 22),

-- 36: Pão de Queijo G (Queijo Mussarela, Ovos)
(36, 17), (36, 22),

-- 37: Quiche de Bacon (Farinha de Trigo, Queijo Mussarela, Bacon, Ovos, Creme de Leite)
(37, 16), (37, 17), (37, 21), (37, 22), (37, 4),

-- 38: Coxinha de Frango (Farinha de Trigo, Frango Desfiado, Requeijão)
(38, 16), (38, 19), (38, 20),

-- 39: Wrap de Frango (Farinha de Trigo, Queijo Mussarela, Frango Desfiado)
(39, 16), (39, 17), (39, 19),

-- 40: Sanduíche Natural (Farinha de Trigo, Frango Desfiado, Requeijão)
(40, 16), (40, 19), (40, 20),

-- 41: Brownie de Chocolate (Chocolate Meio Amargo, Farinha de Trigo, Manteiga, Açúcar, Ovos)
(41, 25), (41, 16), (41, 23), (41, 7), (41, 22),

-- 42: Cookie de Chocolate (Chocolate Meio Amargo, Farinha de Trigo, Manteiga, Açúcar)
(42, 25), (42, 16), (42, 23), (42, 7),

-- 43: Bolo de Cenoura (Farinha de Trigo, Açúcar, Ovos, Chocolate em Pó, Manteiga)
(43, 16), (43, 7), (43, 22), (43, 5), (43, 23),

-- 44: Cheesecake de Doce de Leite (Doce de Leite, Creme de Leite, Açúcar)
(44, 26), (44, 4), (44, 7),

-- 45: Brigadeiro Gourmet (Doce de Leite, Leite Integral, Chocolate em Pó, Manteiga)
(45, 26), (45, 2), (45, 5), (45, 23),

-- 46: Fatia de Torta de Coco (Coco Ralado, Creme de Leite, Açúcar)
(46, 27), (46, 4), (46, 7),

-- 47: Muffin de Baunilha (Calda de Baunilha, Farinha de Trigo, Ovos, Açúcar, Manteiga, Chocolate Meio Amargo)
(47, 13), (47, 16), (47, 22), (47, 7), (47, 23), (47, 25),

-- 48: Pão de Mel (Doce de Leite, Chocolate Meio Amargo, Farinha de Trigo, Açúcar)
(48, 26), (48, 25), (48, 16), (48, 7);

INSERT INTO `personalizacao` (`nome`, `tipo`) VALUES
  ('Sem açúcar',         'açúcar'),
  ('Adoçante',           'açúcar'),
  ('Açúcar adicional',   'açúcar'),
  ('Leite vegetal',      'leite'),
  ('Sem leite',          'leite'),
  ('Mais café',          'café'),
  ('Café fraco',         'café');