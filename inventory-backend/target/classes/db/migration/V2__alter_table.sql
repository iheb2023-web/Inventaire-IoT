-- 1. Supprimer la clé étrangère existante
ALTER TABLE rfid_event
DROP CONSTRAINT IF EXISTS rfid_event_esp32_id_fkey;

-- 2. Changer le type de la colonne
ALTER TABLE rfid_event
ALTER COLUMN esp32_id TYPE VARCHAR(50);

-- 3. (optionnel) NOT NULL
ALTER TABLE rfid_event
ALTER COLUMN esp32_id SET NOT NULL;
