UPDATE grad
SET slug = REGEXP_REPLACE(
    REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
        LOWER(TRIM(ime_grad)),
        'š','s'),'đ','d'),'č','c'),'ć','c'),'ž','z'),
        '\\s+', ' '
        )
WHERE slug IS NULL;