insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'capacitor 1206 4nF 10V', 1, 4, 'nF', 10, 'V', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'capacitor 1206 56nF 10V', 1, 56, 'nF', 10, 'V', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'capacitor 1206 56pF 10V', 1, 56, 'pF', 10, 'V', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'capacitor 0603 4nF 100V', 1, 4, 'nF', 100, 'V', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'resistor 1206 1R 1W 5%', 1, 1, 'Ohm', 1, 'W', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'resistor 0402 100R 3W 5%', 1, 100, 'Ohm', 3, 'W', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'resistor 0603 220K 1W 1%', 1, 220, 'KOhm', 1, 'W', 0.1, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'transformer 220/24V AC 20W', 2, 220, 'V', 24, 'V', 100, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, tolerance, visible)
values ( 'resistor 470k 3W 5%', 2, 470, 'KOhm', 3, 'W', 0.1, 5, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'relay RIF-0-RPT-24DC/ 1', 2, 24, 'V', 9, 'mA', 25, true );
insert into component ("name", type, primary_value, primary_unit, secondary_value, secondary_unit, weight_in_grammes, visible)
values ( 'Electrical tape black 30mx2cm', 3, 30, 'm', 2, 'cm', 30, true );

insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 1, 1, 5000, true, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 2, 1, 300, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 3, 2, 2000, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 4, 3, 4000, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 5, 4, 136, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 6, 5, 5, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 7, 7, 1500, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 8, 8, 200, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 9, 9, 500, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 1, 10, 10, 98, false, true);
insert into storage_unit ("column", "row", shelf, component_id, quantity, full, visible) values (1, 2, 1, 11, 10, false, true);
insert into storage_unit ("column", "row", shelf, full, visible) values (1, 2, 2, false, false); --invisible unit
