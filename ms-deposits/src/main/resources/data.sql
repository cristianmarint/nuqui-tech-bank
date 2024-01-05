DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS deposits;
DROP SEQUENCE IF EXISTS deposits_account_number_sequence;

CREATE SEQUENCE deposits_account_number_sequence;

CREATE TABLE IF NOT EXISTS deposits
(
    id             UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_number VARCHAR(255) DEFAULT ('AC' || nextval('deposits_account_number_sequence')) UNIQUE,
    human_id       VARCHAR(255),
    user_id        VARCHAR(560),
    name           VARCHAR(255),
    balance        VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transactions
(
    id                       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    deposit_id_from          UUID,
    deposit_id_to            UUID,

    status            VARCHAR(255),
    timestamp    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    human_id_from            VARCHAR(255),
    user_id_from             VARCHAR(560),

    human_id_to              VARCHAR(255),
    user_id_to               VARCHAR(560),

    fee                      VARCHAR(255),
    fee_deposit_id_to        UUID,
    amount                   VARCHAR(255),
    total_transaction_amount VARCHAR(255),

    inicial_balance_from       VARCHAR(255),
    final_balance_from         VARCHAR(255),

    inicial_balance_to         VARCHAR(255),
    final_balance_to           VARCHAR(255)


,
    FOREIGN KEY (deposit_id_from) REFERENCES deposits (id),
    FOREIGN KEY (deposit_id_to) REFERENCES deposits (id),
    FOREIGN KEY (fee_deposit_id_to) REFERENCES deposits (id)
);

INSERT INTO deposits (id, account_number, human_id, user_id, name, balance)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a10', '701', '7', '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 'Saving 701',
        '18990.00')
ON CONFLICT (id) DO NOTHING;

INSERT INTO deposits (id, account_number, human_id, user_id, name, balance)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '702', '7', '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 'Saving 702',
        '18990.00')
ON CONFLICT (id) DO NOTHING;

INSERT INTO deposits (id, account_number, human_id, user_id, name, balance)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '987654321', 8, '12ebc8ee-ff66-4455-ad3b-6a7c7413ac98', 'Ahorro',
        10.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO deposits (id, account_number, human_id, user_id, name, balance)
VALUES ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '456789123', 69, 'm1eebc99-9c0b-4ef8-bb6d-6bb9bd380a39',
        'Nuqui Tech Fees', 10.00)
ON CONFLICT (id) DO NOTHING;


INSERT INTO transactions (id, deposit_id_from, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, fee,
                          amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to,
                          final_balance_to, fee_deposit_id_to,status)
VALUES ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 7, '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 8,
        '12ebc8ee-ff66-4455-ad3b-6a7c7413ac98', 1.00, 1.00, 2.00, 10.00, 8.00, 10.00, 11.00,
        'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13','TRANSACTION_STATUS_COMPLETED');

INSERT INTO transactions (id, deposit_id_from, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, fee,
                          amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to,
                          final_balance_to, fee_deposit_id_to,status)
VALUES ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 7, '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 8,
        '12ebc8ee-ff66-4455-ad3b-6a7c7413ac98', 1.00, 1.00, 2.00, 8.00, 6.00, 11.00, 12.00,
        'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13','TRANSACTION_STATUS_COMPLETED');

INSERT INTO transactions (id, deposit_id_from, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, fee,
                          amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to,
                          final_balance_to, fee_deposit_id_to,status)
VALUES ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 7, '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 8,
        '12ebc8ee-ff66-4455-ad3b-6a7c7413ac98', 1.00, 1.00, 2.00, 6.00, 4.00, 12.00, 13.00,
        'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13','TRANSACTION_STATUS_COMPLETED');

INSERT INTO transactions (id, deposit_id_from, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, fee,
                          amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to,
                          final_balance_to, fee_deposit_id_to,status)
VALUES ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 7, '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 8,
        '12ebc8ee-ff66-4455-ad3b-6a7c7413ac98', 1.00, 1.00, 2.00, 4.00, 2.00, 13.00, 14.00,
        'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13','TRANSACTION_STATUS_COMPLETED');

INSERT INTO transactions (id, deposit_id_from, deposit_id_to, human_id_from, user_id_from, human_id_to, user_id_to, fee,
                          amount, total_transaction_amount, inicial_balance_from, final_balance_from, inicial_balance_to,
                          final_balance_to, fee_deposit_id_to,status)
VALUES ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 8, '12ebc8ee-ff66-4455-ad3b-6a7c7413ac98', 7,
        '12ebc8ee-ff66-4455-ad3b-6a7c7413ac97', 1.00, 1.00, 2.00, 14.00, 12.00, 2.00, 4.00,
        'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13','TRANSACTION_STATUS_COMPLETED');