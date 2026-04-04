create sequence route_code_seq;

create function generate_route_code(p_origin_code text, p_destination_code text) returns text
    language plpgsql
as
$$
DECLARE
    v_sequence BIGINT;
    v_sequence_formatted TEXT;
BEGIN
    -- Lấy giá trị tiếp theo từ sequence
    v_sequence := nextval('route_code_seq');

    -- Format thành 2 chữ số (01, 02, 03...)
    v_sequence_formatted := LPAD(v_sequence::TEXT, 2, '0');

    -- Trả về format ORG-DES-XX
    RETURN p_origin_code || '-' || p_destination_code || '-' || v_sequence_formatted;
END;
$$;