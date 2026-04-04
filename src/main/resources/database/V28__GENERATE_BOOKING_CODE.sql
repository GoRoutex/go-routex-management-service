create sequence booking_code_seq;

create function generate_booking_code() returns text
    language plpgsql
as
$$
DECLARE
    v_sequence BIGINT;
    v_sequence_formatted TEXT;
    v_booking_date TEXT;
BEGIN
    v_sequence := nextval('booking_code_seq');
    v_sequence_formatted := LPAD(v_sequence::TEXT, 6, '0');
    v_booking_date := TO_CHAR(NOW(), 'YYYYMMDD');

    RETURN 'BK-' || v_booking_date || '-' || v_sequence_formatted;
END;
$$;

