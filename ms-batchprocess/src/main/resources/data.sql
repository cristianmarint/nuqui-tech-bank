CREATE TABLE IF NOT EXISTS deposit_transactions_batch
(
    id                       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    transaction_id                       UUID,
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
);