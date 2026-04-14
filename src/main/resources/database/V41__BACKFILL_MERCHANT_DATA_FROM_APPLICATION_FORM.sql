WITH matched_application_form AS (
    SELECT
        m.id AS matched_merchant_id,
        maf.id,
        maf.tax_code,
        maf.display_name,
        maf.owner_full_name,
        maf.owner_phone,
        maf.owner_email,
        maf.owner_name,
        maf.contact_name,
        maf.contact_phone,
        maf.contact_email,
        maf.address,
        maf.ward,
        maf.province,
        maf.country,
        maf.postal_code,
        maf.approved_at,
        maf.submitted_at,
        maf.approved_by,
        maf.submitted_by,
        maf.bank_account_name,
        maf.bank_account_number,
        maf.bank_branch,
        maf.bank_name,
        maf.business_license,
        maf.logo_url,
        ROW_NUMBER() OVER (
            PARTITION BY m.id
            ORDER BY COALESCE(maf.approved_at, maf.submitted_at) DESC, maf.id DESC
            ) AS rn
    FROM merchants m
             JOIN merchant_application_form maf
                  ON maf.status = 'APPROVED'
                      AND (
                         (m.tax_code IS NOT NULL AND maf.tax_code IS NOT NULL AND m.tax_code = maf.tax_code)
                             OR (
                             (m.tax_code IS NULL OR BTRIM(m.tax_code) = '')
                                 AND m.name = maf.display_name
                                 AND COALESCE(m.representative_name, '') = COALESCE(maf.owner_full_name, '')
                             )
                         )
)
UPDATE merchants m
SET
    phone = COALESCE(NULLIF(m.phone, ''), maf.contact_phone, maf.owner_phone),
    email = COALESCE(NULLIF(m.email, ''), maf.contact_email, maf.owner_email),
    address = CASE
                  WHEN m.address IS NULL
                      OR BTRIM(m.address) = ''
                      OR m.address = maf.address
                      THEN CONCAT_WS(
                          ', ',
                          NULLIF(BTRIM(maf.address), ''),
                          NULLIF(BTRIM(maf.ward), ''),
                          NULLIF(BTRIM(maf.province), ''),
                          NULLIF(BTRIM(maf.country), ''),
                          NULLIF(BTRIM(maf.postal_code), '')
                           )
                  ELSE m.address
        END,
    representative_name = COALESCE(NULLIF(m.representative_name, ''), maf.owner_full_name, maf.contact_name),
    status = COALESCE(m.status, 'ACTIVE'),
    created_at = COALESCE(m.created_at, maf.approved_at, maf.submitted_at),
    created_by = COALESCE(NULLIF(m.created_by, ''), maf.approved_by, maf.submitted_by, maf.contact_email),
    updated_at = COALESCE(m.updated_at, maf.approved_at, maf.submitted_at),
    updated_by = COALESCE(NULLIF(m.updated_by, ''), maf.approved_by, maf.submitted_by, maf.contact_email),
    contact_email = COALESCE(NULLIF(m.contact_email, ''), maf.contact_email),
    contact_name = COALESCE(NULLIF(m.contact_name, ''), maf.contact_name),
    contact_phone = COALESCE(NULLIF(m.contact_phone, ''), maf.contact_phone),
    owner_email = COALESCE(NULLIF(m.owner_email, ''), maf.owner_email),
    owner_full_name = COALESCE(NULLIF(m.owner_full_name, ''), maf.owner_full_name),
    owner_name = COALESCE(NULLIF(m.owner_name, ''), maf.owner_name),
    owner_phone = COALESCE(NULLIF(m.owner_phone, ''), maf.owner_phone),
    bank_account_name = COALESCE(NULLIF(m.bank_account_name, ''), maf.bank_account_name),
    bank_account_number = COALESCE(NULLIF(m.bank_account_number, ''), maf.bank_account_number),
    bank_branch = COALESCE(NULLIF(m.bank_branch, ''), maf.bank_branch),
    bank_name = COALESCE(NULLIF(m.bank_name, ''), maf.bank_name),
    busienss_license_number = COALESCE(NULLIF(m.busienss_license_number, ''), maf.business_license),
    logo_url = CASE
                   WHEN m.logo_url IS NULL
                       AND maf.logo_url ~ '^[0-9]+$'
                       THEN maf.logo_url::oid
                   ELSE m.logo_url
        END
FROM matched_application_form maf
WHERE maf.rn = 1
  AND m.id = maf.matched_merchant_id
  AND (
    m.phone IS NULL OR BTRIM(m.phone) = ''
        OR m.email IS NULL OR BTRIM(m.email) = ''
        OR m.status IS NULL
        OR m.created_at IS NULL
        OR m.created_by IS NULL OR BTRIM(m.created_by) = ''
        OR m.updated_at IS NULL
        OR m.updated_by IS NULL OR BTRIM(m.updated_by) = ''
        OR m.address IS NULL OR BTRIM(m.address) = '' OR m.address = maf.address
    );