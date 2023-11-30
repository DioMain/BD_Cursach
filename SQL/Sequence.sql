CREATE SEQUENCE DISEASE_ID_SEQUENCE
    MAXVALUE 10000000
    START WITH 1
    order
    CYCLE
    NOCACHE;

CREATE SEQUENCE DIAGNOSE_ID_SEQUENCE
    MAXVALUE 10000000
    START WITH 1
    CYCLE
    NOCACHE;

DROP SEQUENCE DISEASE_ID_SEQUENCE;
DROP SEQUENCE DIAGNOSE_ID_SEQUENCE;