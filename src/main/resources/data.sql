-- 1. Cidades
INSERT INTO cidade (id, nome, estado, populacao)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'São Paulo', 'SP', 11201855);
INSERT INTO cidade (id, nome, estado, populacao)
VALUES ('123e4567-e89b-12d3-a456-426614174100', 'Campinas', 'SP', 1139047);
INSERT INTO cidade (id, nome, estado, populacao)
VALUES ('123e4567-e89b-12d3-a456-426614174200', 'Curitiba', 'PR', 1773718);

-- 2. Linhas
INSERT INTO linha (id, cidade_id, codigo, nome, extensao_km, esta_ativa, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174000', 'L01001', 'Eixo Sul', 12.06, true, 'BRT');
INSERT INTO linha (id, cidade_id, codigo, nome, extensao_km, esta_ativa, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174101', '123e4567-e89b-12d3-a456-426614174000', 'L01002', 'Corredor Paulista', 8.40, true, 'ONIBUS');
INSERT INTO linha (id, cidade_id, codigo, nome, extensao_km, esta_ativa, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174201', '123e4567-e89b-12d3-a456-426614174100', 'L02001', 'Expresso Barão Geraldo', 14.80, true, 'ONIBUS');
INSERT INTO linha (id, cidade_id, codigo, nome, extensao_km, esta_ativa, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174301', '123e4567-e89b-12d3-a456-426614174200', 'L03001', 'Expresso Biarticulado Norte', 19.20, false, 'BRT');

-- 3. Estações / Pontos
INSERT INTO estacao_ponto (id, nome, esta_acessivel, integra_bicicletario)
VALUES ('123e4567-e89b-12d3-a456-426614174002', 'Terminal Santo Amaro', true, true);
INSERT INTO estacao_ponto (id, nome, esta_acessivel, integra_bicicletario)
VALUES ('123e4567-e89b-12d3-a456-426614174102', 'Estação Consolação', true, false);
INSERT INTO estacao_ponto (id, nome, esta_acessivel, integra_bicicletario)
VALUES ('123e4567-e89b-12d3-a456-426614174202', 'Terminal Central Campinas', true, true);

-- 4. Associação N:N (Linha <-> Estação: Terminal Santo Amaro atende 2 linhas)
INSERT INTO linha_ponto (linha_id, ponto_id)
VALUES ('123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174002');
INSERT INTO linha_ponto (linha_id, ponto_id)
VALUES ('123e4567-e89b-12d3-a456-426614174101', '123e4567-e89b-12d3-a456-426614174002');
INSERT INTO linha_ponto (linha_id, ponto_id)
VALUES ('123e4567-e89b-12d3-a456-426614174101', '123e4567-e89b-12d3-a456-426614174102');
INSERT INTO linha_ponto (linha_id, ponto_id)
VALUES ('123e4567-e89b-12d3-a456-426614174201', '123e4567-e89b-12d3-a456-426614174202');

-- 5. Veículos (ativos e inativo)
INSERT INTO veiculo (id, capacidade, esta_ativo, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174003', 180, true, 'BRT');
INSERT INTO veiculo (id, capacidade, esta_ativo, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174103', 90, true, 'ONIBUS');
INSERT INTO veiculo (id, capacidade, esta_ativo, modal)
VALUES ('123e4567-e89b-12d3-a456-426614174203', 160, false, 'BRT');

-- 6. Viagens (cobrindo os 4 estados para demonstração e testes)
INSERT INTO viagem (id, data_hora_inicio, status, linha_id, veiculo_id)
VALUES ('123e4567-e89b-12d3-a456-426614174004', '2026-11-20 08:00:00', 'EM_CURSO', '123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174003');
INSERT INTO viagem (id, data_hora_inicio, status, linha_id, veiculo_id)
VALUES ('123e4567-e89b-12d3-a456-426614174006', '2026-11-27 14:30:00', 'AGENDADA', '123e4567-e89b-12d3-a456-426614174101', '123e4567-e89b-12d3-a456-426614174103');
INSERT INTO viagem (id, data_hora_inicio, status, linha_id, veiculo_id)
VALUES ('123e4567-e89b-12d3-a456-426614174007', '2026-11-26 09:15:00', 'CANCELADA', '123e4567-e89b-12d3-a456-426614174201', '123e4567-e89b-12d3-a456-426614174103');
INSERT INTO viagem (id, data_hora_inicio, status, linha_id, veiculo_id)
VALUES ('123e4567-e89b-12d3-a456-426614174008', '2026-11-25 06:00:00', 'CONCLUIDA', '123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174003');

-- 7. Ocorrências
INSERT INTO ocorrencia (id, resolvida, tipo, gravidade, viagem_id)
VALUES ('123e4567-e89b-12d3-a456-426614174005', false, 'ATRASO', 'MEDIA', '123e4567-e89b-12d3-a456-426614174004');
INSERT INTO ocorrencia (id, resolvida, tipo, gravidade, viagem_id)
VALUES ('123e4567-e89b-12d3-a456-426614174105', true, 'ATRASO', 'MEDIA', '123e4567-e89b-12d3-a456-426614174008');

-- 8. Roles e Usuários
INSERT INTO role (id, nome) VALUES ('123e4567-e89b-12d3-a456-426614174010', 'ROLE_ADMIN');
INSERT INTO role (id, nome) VALUES ('123e4567-e89b-12d3-a456-426614174011', 'ROLE_OPERADOR');

INSERT INTO usuario (id, username, password) VALUES ('123e4567-e89b-12d3-a456-426614174020', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO usuario (id, username, password) VALUES ('123e4567-e89b-12d3-a456-426614174021', 'operador', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO usuario_role (usuario_id, role_id) VALUES ('123e4567-e89b-12d3-a456-426614174020', '123e4567-e89b-12d3-a456-426614174010');
INSERT INTO usuario_role (usuario_id, role_id) VALUES ('123e4567-e89b-12d3-a456-426614174021', '123e4567-e89b-12d3-a456-426614174011');