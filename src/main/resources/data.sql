-- 1. Cidades
INSERT INTO cidade (id, nome, estado, populacao)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'São Paulo', 'SP', 11201855);

-- 2. Linhas
INSERT INTO linha (id, cidade_id, codigo, nome, extensao_km, esta_ativa, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174000', 'L01001', 'Eixo Sul', 12.06, true, 'BRT');

-- 3. Estações / Pontos
INSERT INTO estacao_ponto (id, nome, esta_acessivel, integra_bicicletario)
VALUES ('123e4567-e89b-12d3-a456-426614174002', 'Terminal Santo Amaro', true, true);

-- 4. Associação N:N (Linha <-> Estação)
INSERT INTO linha_ponto (linha_id, ponto_id)
VALUES ('123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174002');

-- 5. Veículos
INSERT INTO veiculo (id, capacidade, esta_ativo, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174003', 180, true, 'BRT');

-- 6. Viagens
INSERT INTO viagem (id, data_hora_inicio, status, linha_id, veiculo_id)
VALUES ('123e4567-e89b-12d3-a456-426614174004', '2026-11-20 08:00:00', 'EM_CURSO', '123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174003');

-- 7. Ocorrências
INSERT INTO ocorrencia (id, resolvida, tipo, gravidade, viagem_id)
VALUES ('123e4567-e89b-12d3-a456-426614174005', false, 'ATRASO', 'MEDIA', '123e4567-e89b-12d3-a456-426614174004');

-- 8. Roles e Usuários (espelhando os perfis de acesso do sistema)
INSERT INTO role (id, nome) VALUES ('123e4567-e89b-12d3-a456-426614174010', 'ROLE_ADMIN');
INSERT INTO role (id, nome) VALUES ('123e4567-e89b-12d3-a456-426614174011', 'ROLE_OPERADOR');

INSERT INTO usuario (id, username, password) VALUES ('123e4567-e89b-12d3-a456-426614174020', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO usuario (id, username, password) VALUES ('123e4567-e89b-12d3-a456-426614174021', 'operador', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO usuario_role (usuario_id, role_id) VALUES ('123e4567-e89b-12d3-a456-426614174020', '123e4567-e89b-12d3-a456-426614174010');
INSERT INTO usuario_role (usuario_id, role_id) VALUES ('123e4567-e89b-12d3-a456-426614174021', '123e4567-e89b-12d3-a456-426614174011');