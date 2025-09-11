CREATE OR REPLACE FUNCTION set_privilege_created_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.createdAt := NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_privilege_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updatedAt := NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION soft_delete_privilege()
RETURNS TRIGGER AS $$
BEGIN
  NEW.deletedAt := NOW();
  NEW.deleted := TRUE;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- On INSERT: Set createdAt
CREATE TRIGGER trg_privilege_created_at
BEFORE INSERT ON privilege
FOR EACH ROW
EXECUTE FUNCTION set_privilege_created_at();

-- On UPDATE: Set updatedAt
CREATE TRIGGER trg_privilege_updated_at
BEFORE UPDATE ON privilege
FOR EACH ROW
EXECUTE FUNCTION set_privilege_updated_at();

-- On SOFT DELETE (update `deleted = true`)
CREATE TRIGGER trg_privilege_soft_delete
BEFORE UPDATE ON privilege
FOR EACH ROW
WHEN (OLD.deleted = FALSE AND NEW.deleted = TRUE)
EXECUTE FUNCTION soft_delete_privilege();

