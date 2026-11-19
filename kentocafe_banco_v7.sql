-- ============================================================================
-- KENTOCAFE — SCRIPT COMPLETO DO BANCO DE DADOS
-- ============================================================================
-- Representa o banco inteiro na forma final.
--
-- ATENÇÃO — CREATE TABLE IF NOT EXISTS só cria a tabela se ela não existir;
-- se você já tem `produto` ou `item_pedido` criadas SEM as colunas novas
-- (ativo / tamanho_id), rodar este script não vai adicionar essas colunas,
-- porque a tabela já existe e o comando é ignorado. Nesse caso, use as
-- instruções ALTER TABLE ou apague as tabelas afetadas
-- antes de rodar este script. Para um banco novo (do zero), este script
-- já contempla tudo.
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS `cafeteria`;
USE `cafeteria`;

-- ============================================================================
-- FUNCIONÁRIOS
-- ============================================================================
CREATE TABLE IF NOT EXISTS `funcionario` (
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `nome`    VARCHAR(50)  NOT NULL,
    `email`   VARCHAR(254) NOT NULL,
    `senha`   VARCHAR(128) NOT NULL,
    `gerente` TINYINT      NOT NULL,
    `ativo`   TINYINT	   NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`),
    UNIQUE (`email`)
);

-- ============================================================================
-- CARDÁPIO
-- ============================================================================

-- Tipos possíveis de categoria
CREATE TABLE IF NOT EXISTS `tipo_categoria` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`nome`)
);
INSERT IGNORE INTO `tipo_categoria` (`nome`) VALUES ('Bebidas'), ('Iguarias'), ('Ambos');

-- Ingredientes (catálogo)
CREATE TABLE IF NOT EXISTS `ingrediente` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Catálogo global de personalizações
CREATE TABLE IF NOT EXISTS `personalizacao` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(60) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`nome`)
);

-- Tamanhos de copo disponíveis para bebidas
CREATE TABLE IF NOT EXISTS `tamanho` (
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `nome`      VARCHAR(20) NOT NULL,
    `volume_ml` INT         NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`nome`)
);
INSERT IGNORE INTO `tamanho` (`nome`, `volume_ml`) VALUES
    ('Pequeno', 200),
    ('Médio',   300),
    ('Grande',  500);

-- Categorias de produto
CREATE TABLE IF NOT EXISTS `categoria` (
    `id`                BIGINT      NOT NULL AUTO_INCREMENT,
    `nome`              VARCHAR(50) NOT NULL,
    `tipo_categoria_id` BIGINT      NOT NULL DEFAULT 3, -- 3 = Ambos
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_categoria_tipo` FOREIGN KEY (`tipo_categoria_id`) REFERENCES `tipo_categoria`(`id`)
);

-- Ingredientes padrão de uma categoria — copiados para o produto na criação
CREATE TABLE IF NOT EXISTS `categoria_ingrediente` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `categoria_id`   BIGINT NOT NULL,
    `ingrediente_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`categoria_id`, `ingrediente_id`),
    CONSTRAINT `fk_ci_categoria`   FOREIGN KEY (`categoria_id`)   REFERENCES `categoria`(`id`),
    CONSTRAINT `fk_ci_ingrediente` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente`(`id`)
);

-- Produtos
CREATE TABLE IF NOT EXISTS `produto` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `nome`          VARCHAR(45)  NOT NULL,
    `categoria_id`  BIGINT       NOT NULL,
    `preco_unidade` DECIMAL(5,2) NULL,
    `descricao`     VARCHAR(200) NULL,
    `path_ft`       VARCHAR(200) NULL,
    `ativo`         TINYINT      NOT NULL DEFAULT 1, -- soft delete: 1 = ativo, 0 = desativado
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_produto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria`(`id`)
);

-- Composição real de ingredientes de cada produto
CREATE TABLE IF NOT EXISTS `produto_ingrediente` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `produto_id`     BIGINT NOT NULL,
    `ingrediente_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`produto_id`, `ingrediente_id`),
    CONSTRAINT `fk_pi_produto`     FOREIGN KEY (`produto_id`)     REFERENCES `produto`(`id`),
    CONSTRAINT `fk_pi_ingrediente` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente`(`id`)
);

-- Personalizações que cada produto pode oferecer
CREATE TABLE IF NOT EXISTS `produto_personalizacao` (
    `id`                BIGINT NOT NULL AUTO_INCREMENT,
    `produto_id`        BIGINT NOT NULL,
    `personalizacao_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`produto_id`, `personalizacao_id`),
    CONSTRAINT `fk_pp_produto`        FOREIGN KEY (`produto_id`)        REFERENCES `produto`(`id`),
    CONSTRAINT `fk_pp_personalizacao` FOREIGN KEY (`personalizacao_id`) REFERENCES `personalizacao`(`id`)
);

-- Tamanhos e preços que cada produto (bebida) oferece
CREATE TABLE IF NOT EXISTS `produto_tamanho` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `produto_id`    BIGINT       NOT NULL,
    `tamanho_id`    BIGINT       NOT NULL,
    `preco_unidade` DECIMAL(5,2) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`produto_id`, `tamanho_id`),
    CONSTRAINT `fk_pt_produto` FOREIGN KEY (`produto_id`) REFERENCES `produto`(`id`),
    CONSTRAINT `fk_pt_tamanho` FOREIGN KEY (`tamanho_id`) REFERENCES `tamanho`(`id`)
);

-- ============================================================================
-- PEDIDO
-- ============================================================================

-- Status possíveis do pedido
CREATE TABLE IF NOT EXISTS `status` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`)
);
INSERT IGNORE INTO `status` (`nome`) VALUES ('Em preparo'), ('Pronto'), ('Cancelado');

-- Pedidos
CREATE TABLE IF NOT EXISTS `pedido` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `nome_cliente`   VARCHAR(45)  NOT NULL,
    `dt_hr_pedido`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `dt_hr_pronto`   DATETIME     NULL,
    `valor_total`    DECIMAL(6,2) NULL,
    `status_id`      BIGINT       NOT NULL DEFAULT 1,
    `funcionario_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_pedido_status`      FOREIGN KEY (`status_id`)      REFERENCES `status`(`id`),
    CONSTRAINT `fk_pedido_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `funcionario`(`id`)
);

-- Itens de um pedido
CREATE TABLE IF NOT EXISTS `item_pedido` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `quantidade`    INT          NOT NULL,
    `preco_unidade` DECIMAL(5,2) NOT NULL,
    `pedido_id`     BIGINT       NOT NULL,
    `produto_id`    BIGINT       NOT NULL,
    `tamanho_id`    BIGINT       NULL, -- preenchido só para bebidas com tamanho
    PRIMARY KEY (`id`),
    UNIQUE (`produto_id`, `pedido_id`),
    CONSTRAINT `fk_item_pedido`   FOREIGN KEY (`pedido_id`)  REFERENCES `pedido`(`id`),
    CONSTRAINT `fk_item_produto`  FOREIGN KEY (`produto_id`) REFERENCES `produto`(`id`),
    CONSTRAINT `fk_item_tamanho`  FOREIGN KEY (`tamanho_id`) REFERENCES `tamanho`(`id`)
);

-- Personalizações escolhidas em cada item do pedido
CREATE TABLE IF NOT EXISTS `item_pedido_personalizacao` (
    `id`                BIGINT NOT NULL AUTO_INCREMENT,
    `item_pedido_id`    BIGINT NOT NULL,
    `personalizacao_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`item_pedido_id`, `personalizacao_id`),
    CONSTRAINT `fk_ip_itemPedido`     FOREIGN KEY (`item_pedido_id`)    REFERENCES `item_pedido`(`id`),
    CONSTRAINT `fk_ip_personalizacao` FOREIGN KEY (`personalizacao_id`) REFERENCES `personalizacao`(`id`)
);
