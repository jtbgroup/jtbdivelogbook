package be.vds.jtbdive;

import java.util.ArrayList;
import java.util.List;

import be.vds.jtbdive.client.view.core.logbook.material.MaterialSelectionDialog;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.DefaultSizeableMaterial;
import be.vds.jtbdive.core.core.material.DiveTankMaterial;
import be.vds.jtbdive.core.core.material.FinsMaterial;
import be.vds.jtbdive.core.core.material.MaskMaterial;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.core.material.MaterialSet;

public class MaterialSelectionDialogTester {

	public static void main(String[] args) {
		List<Material> materials = new ArrayList<Material>();
		List<MaterialSet> materialSets = new ArrayList<MaterialSet>();

		MaterialSet ms1 = new MaterialSet("set 1");
		MaterialSet ms2 = new MaterialSet("set 2");
		materialSets.add(ms1);
		materialSets.add(ms2);
		
		Material m = new DiveTankMaterial();
		m.setManufacturer("Scuba");
		materials.add(m);
		ms1.addMaterial(m);

		m = new DiveTankMaterial();
		m.setManufacturer("Mares");
		materials.add(m);
		ms1.addMaterial(m);

		m = new FinsMaterial();
		m.setManufacturer("Mares");
		materials.add(m);
		ms1.addMaterial(m);

		m = new MaskMaterial();
		m.setManufacturer("Cressi");
		materials.add(m);
		ms1.addMaterial(m);

		m = new MaskMaterial();
		m.setManufacturer("Scuba");
		materials.add(m);
		ms2.addMaterial(m);

		m = new DefaultSizeableMaterial(MaterialType.BUOYANCY_COMPENSATOR);
		m.setManufacturer("Dive Rite");
		materials.add(m);
		ms2.addMaterial(m);

		MaterialSelectionDialog dlg = new MaterialSelectionDialog();
		dlg.setMaterials(materials);
		dlg.setMaterialSets(materialSets);

		dlg.setLocationRelativeTo(null);
		dlg.showDialog(400, 250);
	}
	
}
