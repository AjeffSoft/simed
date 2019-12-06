ALTER TABLE `simed`.`conta_pagar` 
DROP FOREIGN KEY `destino_ibfk_3`,
DROP FOREIGN KEY `conta_pagar_ibfk_2`;
DROP FOREIGN KEY `conta_pagar_ibfk_5`;
ALTER TABLE `simed`.`conta_pagar` 
DROP COLUMN `destino`,
DROP COLUMN `reter_cofins`,
DROP COLUMN `iss_porcentagem`,
DROP COLUMN `reter_inss`,
DROP COLUMN `reter_ir`,
DROP COLUMN `valor_cofins`,
DROP COLUMN `valor_iss`,
DROP COLUMN `valor_inss`,
DROP COLUMN `valor_ir`,
DROP COLUMN `anotacao_pagamento`,
DROP COLUMN `id_evento`,
DROP COLUMN `tipo_documento`,
DROP COLUMN `anexo`,
DROP COLUMN `data_pago`,
DROP COLUMN `id_imposto_gerado`,
DROP INDEX `destino_ibfk_3` ,
DROP INDEX `id_evento` ;
DROP INDEX `conta_pagar_ibfk_5_idx` ;