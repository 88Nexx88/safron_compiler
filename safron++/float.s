VAR FLOAT r;

{
    r = 0.0;

    WHILE(r <= 30.0) DO {

        IF(r >= 15.1) THEN{
            BREAK;
        }
        r = r + 1.5;
    }
    PRINT(r);
}.