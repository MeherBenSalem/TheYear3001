// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelsentinel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "sentinel"), "main");
	private final ModelPart all;
	private final ModelPart leg1;
	private final ModelPart leg1_1;
	private final ModelPart leg2;
	private final ModelPart leg1_2;
	private final ModelPart body;
	private final ModelPart ribs;
	private final ModelPart chest_armor;
	private final ModelPart arm1;
	private final ModelPart arm1_1;
	private final ModelPart razor1;
	private final ModelPart razor1_2;
	private final ModelPart arm2;
	private final ModelPart arm1_2;
	private final ModelPart razor2;
	private final ModelPart razor1_3;
	private final ModelPart neck;
	private final ModelPart h_head;

	public Modelsentinel(ModelPart root) {
		this.all = root.getChild("all");
		this.leg1 = this.all.getChild("leg1");
		this.leg1_1 = this.leg1.getChild("leg1_1");
		this.leg2 = this.all.getChild("leg2");
		this.leg1_2 = this.leg2.getChild("leg1_2");
		this.body = this.all.getChild("body");
		this.ribs = this.body.getChild("ribs");
		this.chest_armor = this.body.getChild("chest_armor");
		this.arm1 = this.body.getChild("arm1");
		this.arm1_1 = this.arm1.getChild("arm1_1");
		this.razor1 = this.arm1_1.getChild("razor1");
		this.razor1_2 = this.razor1.getChild("razor1_2");
		this.arm2 = this.body.getChild("arm2");
		this.arm1_2 = this.arm2.getChild("arm1_2");
		this.razor2 = this.arm1_2.getChild("razor2");
		this.razor1_3 = this.razor2.getChild("razor1_3");
		this.neck = this.body.getChild("neck");
		this.h_head = this.neck.getChild("h_head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leg1 = all.addOrReplaceChild("leg1",
				CubeListBuilder.create().texOffs(58, 72)
						.addBox(-1.0F, 8.5F, -1.5F, 0.75F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(58, 72)
						.addBox(0.25F, 8.5F, -1.5F, 0.75F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(39, 72)
						.mirror().addBox(-0.25F, 8.5F, -1.5F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(23, 59).addBox(-1.0F, 0.5F, -1.5F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(66, 21).addBox(1.0F, 6.25F, -2.25F, 1.0F, 1.0F, 2.75F, new CubeDeformation(0.0F))
						.texOffs(65, 32).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(63, 70).addBox(-1.0F, 0.5F, 0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(15, 81).addBox(0.25F, 8.5F, -1.75F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F))
						.texOffs(19, 81).addBox(-1.0F, 8.5F, -1.75F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F))
						.texOffs(23, 81).addBox(-1.0F, 8.5F, 0.5F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F))
						.texOffs(42, 81).addBox(0.25F, 8.5F, 0.5F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)),
				PartPose.offset(3.5F, -24.5F, 2.5F));

		PartDefinition leg1_1 = leg1.addOrReplaceChild("leg1_1", CubeListBuilder.create().texOffs(69, 0)
				.addBox(-1.5F, 13.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(54, 32)
				.addBox(-1.5F, 14.0F, -7.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(28, 12)
				.addBox(-1.5F, 13.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 29)
				.addBox(-1.0F, 13.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(70, 48)
				.addBox(-1.0F, 12.0F, -5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(70, 17)
				.addBox(-0.5F, 12.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(70, 37)
				.addBox(-0.5F, 13.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(57, 18)
				.addBox(-1.0F, 11.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 39).mirror()
				.addBox(-1.0F, 7.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(71, 58)
				.addBox(-1.0F, 1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(56, 80)
				.addBox(-0.25F, 2.0F, -0.25F, 0.5F, 5.0F, 0.5F, new CubeDeformation(0.0F)).texOffs(38, 80)
				.addBox(0.25F, 6.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(1, 81).mirror()
				.addBox(0.25F, 4.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false).texOffs(22, 81)
				.addBox(0.25F, 2.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(64, 80)
				.addBox(0.25F, 5.75F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 71)
				.addBox(0.25F, 3.75F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(74, 80)
				.addBox(-0.25F, 5.5F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(16, 81)
				.addBox(-0.25F, 3.5F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 2)
				.addBox(-0.75F, 5.25F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 31)
				.addBox(-0.75F, 3.25F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 8)
				.addBox(-0.75F, 5.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 54)
				.addBox(-0.75F, 3.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 19)
				.addBox(-0.75F, 4.75F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(80, 25)
				.addBox(-0.75F, 2.75F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(44, 80)
				.addBox(-0.25F, 6.5F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(10, 81)
				.addBox(-0.25F, 4.5F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(34, 81)
				.addBox(-0.25F, 2.5F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(58, 80)
				.addBox(0.25F, 6.25F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(4, 81)
				.addBox(0.25F, 4.25F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(28, 81)
				.addBox(0.25F, 2.25F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(27, 65)
				.addBox(1.0F, 2.0F, -1.0F, 1.0F, 7.0F, 2.5F, new CubeDeformation(0.0F)).texOffs(39, 27)
				.addBox(0.0F, 6.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(47, 48)
				.addBox(0.0F, 7.0F, 1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(41, 44)
				.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(71, 46)
				.addBox(-2.0F, 2.0F, 1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(43, 31)
				.addBox(-1.0F, 4.0F, -2.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 35)
				.addBox(-1.0F, 5.0F, 1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(31, 25)
				.addBox(-2.0F, 2.0F, -1.0F, 1.0F, 6.0F, 2.5F, new CubeDeformation(0.0F)).texOffs(51, 16).addBox(-1.5F,
						14.0F, -1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 9.5F, -0.5F));

		PartDefinition leg1_1_r1 = leg1_1.addOrReplaceChild("leg1_1_r1",
				CubeListBuilder.create().texOffs(28, 80).addBox(-0.25F, 0.25F, -1.5F, 0.5F, 0.5F, 1.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.2071F, 14.0F, -4.5858F, 0.0F, -0.7854F, 0.0F));

		PartDefinition leg1_1_r2 = leg1_1.addOrReplaceChild("leg1_1_r2",
				CubeListBuilder.create().texOffs(22, 80).addBox(-0.25F, 0.25F, -1.5F, 0.5F, 0.5F, 1.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.2071F, 14.0F, -4.5858F, 0.0F, 0.7854F, 0.0F));

		PartDefinition leg2 = all.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(59, 14)
				.addBox(-1.0F, 8.5F, -1.5F, 0.75F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(59, 22)
				.addBox(0.25F, 8.5F, -1.5F, 0.75F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(59, 34)
				.addBox(-0.25F, 8.5F, -1.5F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(34, 40)
				.addBox(-1.0F, 0.5F, -1.5F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(44, 12)
				.addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(72, 1)
				.addBox(0.25F, 8.5F, -1.75F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)).texOffs(4, 3).mirror()
				.addBox(-1.0F, 8.5F, -1.75F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)).mirror(false).texOffs(14, 5)
				.addBox(-1.0F, 8.5F, 0.5F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)).texOffs(28, 7)
				.addBox(0.25F, 8.5F, 0.5F, 0.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)),
				PartPose.offset(-3.5F, -24.5F, 2.5F));

		PartDefinition leg1_2 = leg2.addOrReplaceChild("leg1_2", CubeListBuilder.create().texOffs(72, 18)
				.addBox(-1.5F, 13.0F, 0.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(55, 40)
				.addBox(-1.5F, 14.0F, -7.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(81, 5)
				.addBox(-1.5F, 13.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 60)
				.addBox(-1.0F, 13.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(73, 4)
				.addBox(-1.0F, 12.0F, -5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(73, 11)
				.addBox(-0.5F, 12.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(73, 17)
				.addBox(-0.5F, 13.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(60, 50)
				.addBox(-1.0F, 11.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(7, 61)
				.addBox(-1.0F, 7.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(82, 72)
				.addBox(-1.0F, 10.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 67)
				.addBox(1.0F, 9.25F, -1.0F, 1.0F, 1.75F, 3.0F, new CubeDeformation(0.0F)).texOffs(73, 3)
				.addBox(1.0F, 10.25F, 2.0F, 1.0F, 1.75F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 37)
				.addBox(-1.0F, 10.25F, 3.0F, 2.0F, 1.75F, 1.0F, new CubeDeformation(0.0F)).texOffs(73, 5)
				.addBox(-2.0F, 10.25F, 2.0F, 1.0F, 1.75F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 17)
				.addBox(-2.25F, 10.5F, 2.0F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 23)
				.addBox(2.0F, 10.5F, 2.0F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 67)
				.addBox(-2.0F, 9.25F, -1.0F, 1.0F, 1.75F, 3.0F, new CubeDeformation(0.0F)).texOffs(73, 61)
				.addBox(-1.0F, 1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(1, 41).mirror()
				.addBox(-0.25F, 2.0F, -0.25F, 0.5F, 5.0F, 0.5F, new CubeDeformation(0.0F)).mirror(false).texOffs(81, 47)
				.addBox(-0.75F, 6.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(81, 53)
				.addBox(-0.75F, 4.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(81, 59)
				.addBox(-0.75F, 2.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(81, 65)
				.addBox(-0.75F, 5.75F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(81, 71)
				.addBox(-0.75F, 3.75F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(81, 77)
				.addBox(-0.25F, 5.5F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(2, 82).mirror()
				.addBox(-0.25F, 3.5F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false).texOffs(5, 82)
				.addBox(0.25F, 5.25F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(11, 82)
				.addBox(0.25F, 3.25F, 0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(17, 82)
				.addBox(0.25F, 5.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(23, 82)
				.addBox(0.25F, 3.0F, -0.25F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(29, 82)
				.addBox(0.25F, 4.75F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(35, 82)
				.addBox(0.25F, 2.75F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(41, 82)
				.addBox(-0.25F, 6.5F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(47, 82)
				.addBox(-0.25F, 4.5F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(53, 82)
				.addBox(-0.25F, 2.5F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(59, 82)
				.addBox(-0.75F, 6.25F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(65, 82)
				.addBox(-0.75F, 4.25F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(71, 82)
				.addBox(-0.75F, 2.25F, -0.75F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(43, 39)
				.addBox(1.0F, 2.0F, -1.0F, 1.0F, 3.0F, 2.5F, new CubeDeformation(0.0F)).texOffs(76, 27)
				.addBox(-2.0F, 6.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(78, 31)
				.addBox(-2.0F, 7.0F, 1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 16)
				.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(73, 61)
				.addBox(-2.0F, 2.0F, 1.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 54)
				.addBox(-2.0F, 4.0F, -2.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(82, 58)
				.addBox(-2.0F, 5.0F, 1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 62).mirror()
				.addBox(-2.0F, 2.0F, -1.0F, 1.0F, 6.0F, 2.5F, new CubeDeformation(0.0F)).mirror(false).texOffs(22, 0)
				.addBox(-1.5F, 14.0F, -1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 9.5F, -0.5F));

		PartDefinition leg1_2_r1 = leg1_2.addOrReplaceChild("leg1_2_r1",
				CubeListBuilder.create().texOffs(81, 21).addBox(-0.25F, 0.25F, -1.5F, 0.5F, 0.5F, 1.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.2071F, 14.0F, -4.5858F, 0.0F, -0.7854F, 0.0F));

		PartDefinition leg1_2_r2 = leg1_2.addOrReplaceChild("leg1_2_r2",
				CubeListBuilder.create().texOffs(81, 15).addBox(-0.25F, 0.25F, -1.5F, 0.5F, 0.5F, 1.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.2071F, 14.0F, -4.5858F, 0.0F, 0.7854F, 0.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(74, 2)
				.addBox(2.5F, -0.4667F, -0.5F, 2.0F, 1.5F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 74)
				.addBox(-4.5F, -0.4667F, -0.5F, 2.0F, 1.5F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 19)
				.addBox(-2.5F, -0.9667F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(84, 14)
				.addBox(-2.5F, 0.0333F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(74, 42)
				.addBox(-1.0F, -2.9667F, 0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(75, 62)
				.addBox(-1.0F, -1.9667F, -1.75F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(83, 37)
				.addBox(1.0F, -1.7167F, -0.75F, 1.0F, 0.75F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 23)
				.addBox(-0.25F, -2.4667F, -0.75F, 1.75F, 0.75F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 51)
				.addBox(-0.25F, -3.4667F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(61, 84)
				.addBox(-1.0303F, -3.6435F, -0.5F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(77, 84)
				.addBox(0.3839F, -5.0577F, -0.5F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(69, 84)
				.addBox(0.8839F, -5.0541F, -0.5F, 0.6036F, 0.4964F, 0.5F, new CubeDeformation(0.0F)).texOffs(13, 84)
				.addBox(0.9874F, -7.5041F, -0.5F, 0.5F, 2.45F, 0.5F, new CubeDeformation(0.0F)).texOffs(13, 84)
				.addBox(1.9017F, -10.9112F, -0.5F, 0.5F, 2.7F, 0.5F, new CubeDeformation(0.0F)).texOffs(84, 5)
				.addBox(1.9017F, -11.4112F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 13)
				.addBox(0.5178F, -12.2951F, 0.0F, 0.5F, 1.1F, 0.5F, new CubeDeformation(0.0F)).texOffs(35, 23)
				.addBox(0.5178F, -15.0022F, 0.9142F, 0.5F, 1.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(86, 3)
				.addBox(1.0178F, -15.0022F, 0.9142F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(84, 19)
				.addBox(0.5178F, -12.7951F, 0.0F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(84, 29)
				.addBox(0.841F, -12.4719F, 0.0F, 0.5F, 0.5F, 0.5F, new CubeDeformation(0.0F)).texOffs(75, 53)
				.addBox(-1.25F, -10.4667F, 0.25F, 0.9F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 67)
				.addBox(-1.25F, -9.9667F, -0.75F, 0.9F, 1.25F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 62)
				.addBox(-0.35F, -10.4667F, -0.75F, 2.2F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(41, 84)
				.addBox(0.15F, -7.7645F, -0.0905F, 0.25F, 0.25F, 0.25F, new CubeDeformation(0.0F)).texOffs(2, 84)
				.mirror().addBox(-1.1F, -8.2645F, 0.2595F, 0.45F, 0.5F, 0.25F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(17, 84).addBox(-0.6F, -8.2645F, 0.1595F, 0.25F, 0.5F, 0.25F, new CubeDeformation(0.0F))
				.texOffs(35, 84).addBox(0.15F, -8.2645F, -0.0905F, 0.25F, 0.5F, 0.25F, new CubeDeformation(0.0F))
				.texOffs(48, 26).addBox(-2.35F, -7.2167F, -1.25F, 3.2F, 1.75F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(47, 84).addBox(0.4F, -7.2167F, -1.35F, 0.2F, 1.0F, 0.1F, new CubeDeformation(0.0F))
				.texOffs(4, 84).addBox(-0.1F, -7.2167F, -1.35F, 0.2F, 1.75F, 0.1F, new CubeDeformation(0.0F))
				.texOffs(75, 30).addBox(-2.45F, -7.5667F, -1.35F, 3.4F, 0.35F, 2.2F, new CubeDeformation(0.0F))
				.texOffs(83, 39).addBox(-0.25F, -11.2167F, -0.25F, 2.1F, 0.75F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(83, 61).addBox(1.85F, -10.4667F, -0.25F, 0.5F, 0.75F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(83, 43).addBox(-2.0F, -1.7167F, -0.75F, 1.0F, 0.75F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(67, 44).addBox(-1.0F, -5.9667F, 1.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(84, 1).addBox(-6.0F, -13.4667F, 1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 29).addBox(-4.0F, -14.4667F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 45).addBox(3.0F, -14.4667F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(84, 12).addBox(3.0F, -13.4667F, 1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(76, 2).addBox(-9.0F, -13.4667F, 0.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(76, 17).addBox(7.0F, -13.4667F, 0.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(84, 49).addBox(-9.0F, -14.4667F, 1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(84, 61).addBox(7.0F, -14.4667F, 1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(78, 61).addBox(6.0F, -14.7167F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(62, 42).addBox(-7.0F, -14.4667F, 0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(62, 55).addBox(6.0F, -14.4667F, 0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(85, 78).addBox(1.0F, -13.7167F, 3.5F, 2.0F, 0.5F, 0.25F, new CubeDeformation(0.0F))
				.texOffs(85, 78).addBox(-3.0F, -13.7167F, 3.5F, 2.0F, 0.5F, 0.25F, new CubeDeformation(0.0F))
				.texOffs(82, 39).addBox(-1.5F, -0.9667F, -2.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(82, 79).addBox(2.5F, -0.9667F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(82, 73).addBox(-3.5F, -0.9667F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -25.0333F, 2.5F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1",
				CubeListBuilder.create().texOffs(75, 76)
						.addBox(-0.25F, -0.25F, -0.75F, 0.5F, 0.5F, 1.75F, new CubeDeformation(0.0F)).texOffs(75, 68)
						.addBox(0.5F, -0.25F, -0.75F, 0.5F, 0.5F, 1.75F, new CubeDeformation(0.0F)).texOffs(75, 49)
						.addBox(1.25F, -0.25F, -0.75F, 0.5F, 0.5F, 1.75F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -9.9667F, -0.75F, -0.7854F, 0.0F, 0.0F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2",
				CubeListBuilder.create().texOffs(23, 84).addBox(-0.225F, -0.5F, -0.125F, 0.25F, 0.35F, 0.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.375F, -7.3504F, 0.1027F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body_r3 = body.addOrReplaceChild("body_r3",
				CubeListBuilder.create().texOffs(5, 84).addBox(-0.225F, -0.5F, -0.125F, 0.45F, 0.35F, 0.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.875F, -7.3504F, 0.2027F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body_r4 = body.addOrReplaceChild("body_r4",
				CubeListBuilder.create().texOffs(29, 84).addBox(-0.225F, -0.35F, -0.125F, 0.25F, 0.35F, 0.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.375F, -8.2167F, 0.025F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body_r5 = body.addOrReplaceChild("body_r5",
				CubeListBuilder.create().texOffs(11, 84).addBox(-0.225F, -0.35F, -0.125F, 0.25F, 0.35F, 0.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.375F, -8.2167F, 0.275F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body_r6 = body.addOrReplaceChild("body_r6",
				CubeListBuilder.create().texOffs(83, 79).addBox(-0.225F, -0.35F, -0.125F, 0.45F, 0.35F, 0.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.875F, -8.2167F, 0.375F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body_r7 = body.addOrReplaceChild("body_r7",
				CubeListBuilder.create().texOffs(84, 76).addBox(-0.875F, -0.25F, -0.25F, 1.5F, 0.5F, 0.5F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.7829F, -11.6764F, 0.25F, 0.0F, 0.0F, 0.7854F));

		PartDefinition body_r8 = body.addOrReplaceChild("body_r8",
				CubeListBuilder.create().texOffs(30, 21).addBox(-0.25F, -1.25F, -0.25F, 0.5F, 1.5F, 0.5F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.7678F, -12.7951F, 0.3535F, -0.7854F, 0.0F, 0.0F));

		PartDefinition body_r9 = body.addOrReplaceChild("body_r9",
				CubeListBuilder.create().texOffs(18, 84).addBox(-0.25F, -1.25F, -0.25F, 0.5F, 1.5F, 0.5F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.341F, -7.5041F, -0.25F, 0.0F, 0.0F, 0.7854F));

		PartDefinition body_r10 = body.addOrReplaceChild("body_r10",
				CubeListBuilder.create().texOffs(8, 84).addBox(-0.25F, -1.75F, -0.25F, 0.5F, 2.0F, 0.5F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.6768F, -3.6435F, -0.25F, 0.0F, 0.0F, 0.7854F));

		PartDefinition body_r11 = body.addOrReplaceChild("body_r11",
				CubeListBuilder.create().texOffs(55, 84).addBox(-0.25F, -0.5F, -0.25F, 0.5F, 1.0F, 0.5F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -2.9667F, -0.25F, 0.0F, 0.0F, -0.7854F));

		PartDefinition ribs = body.addOrReplaceChild("ribs",
				CubeListBuilder.create().texOffs(41, 71)
						.addBox(-4.75F, -11.0F, 0.75F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 83)
						.addBox(-5.75F, -11.0F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(2, 75)
						.addBox(-6.75F, -11.0F, -2.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(52, 83)
						.addBox(-5.75F, -11.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(57, 68)
						.addBox(-4.75F, -11.0F, -4.25F, 2.5F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 2)
						.addBox(-2.75F, -13.0F, -3.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 76)
						.addBox(-4.75F, -14.0F, -3.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(59, 68)
						.addBox(-5.75F, -14.0F, -2.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(47, 76)
						.addBox(-4.75F, -14.0F, 0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 79)
						.addBox(-2.75F, -15.0F, 0.75F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 55)
						.addBox(-2.75F, -15.0F, 1.75F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 12)
						.addBox(-4.75F, -14.0F, 1.75F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 76)
						.addBox(-0.75F, -14.0F, 0.75F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 19)
						.addBox(-0.75F, -14.0F, 1.75F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 27)
						.addBox(-0.75F, -14.0F, 0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 76)
						.addBox(-0.75F, -14.0F, -3.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 68)
						.addBox(1.25F, -14.0F, -2.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(23, 59)
						.addBox(-0.75F, -11.0F, 0.75F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 83)
						.addBox(1.25F, -11.0F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(74, 65)
						.addBox(2.25F, -11.0F, -2.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(34, 83)
						.addBox(1.25F, -11.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 52)
						.addBox(-1.25F, -11.0F, -4.25F, 2.5F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 55)
						.addBox(-1.25F, -12.0F, -3.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 49)
						.addBox(-3.25F, -12.0F, -3.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 83)
						.addBox(-0.75F, -9.0F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 83)
						.addBox(0.25F, -9.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(29, 75)
						.addBox(1.25F, -9.0F, -2.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(59, 75)
						.addBox(-0.75F, -9.0F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 85)
						.addBox(-0.5F, -9.0F, 0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 85)
						.addBox(-4.0F, -9.0F, 0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(74, 53)
						.addBox(-2.75F, -12.0F, 0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 47)
						.addBox(-2.75F, -10.0F, -0.25F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 75)
						.addBox(-4.75F, -7.0F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(75, 75)
						.addBox(-4.75F, -9.0F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 75)
						.addBox(-5.75F, -9.0F, -2.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(75, 75)
						.addBox(-5.75F, -7.0F, -2.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(83, 4)
						.addBox(-0.75F, -7.0F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(75, 10)
						.addBox(1.25F, -7.0F, -2.25F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(83, 19)
						.addBox(0.25F, -7.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 5)
						.addBox(-4.75F, -7.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 31)
						.addBox(-3.75F, -7.0F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 85)
						.addBox(1.25F, -7.0F, -3.25F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 69)
						.addBox(2.25F, -6.75F, -2.25F, 0.25F, 0.5F, 3.0F, new CubeDeformation(0.0F)).texOffs(85, 17)
						.addBox(-4.75F, -11.0F, -4.5F, 6.0F, 1.0F, 0.25F, new CubeDeformation(0.0F)).texOffs(80, 83)
						.addBox(-4.75F, -9.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(83, 25)
						.addBox(-3.75F, -9.0F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(1.75F, 0.0333F, 0.75F));

		PartDefinition chest_armor = body.addOrReplaceChild("chest_armor", CubeListBuilder.create(),
				PartPose.offset(7.75F, -1.4667F, 0.5F));

		PartDefinition arm1 = body.addOrReplaceChild("arm1",
				CubeListBuilder.create().texOffs(85, 7)
						.addBox(-1.0F, 0.0167F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(85, 3)
						.addBox(-1.0F, 0.0167F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(72, 26)
						.addBox(-1.0F, 1.0167F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 34)
						.addBox(-1.0F, 2.0167F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(8.0F, -13.4833F, 1.5F));

		PartDefinition arm1_1 = arm1.addOrReplaceChild("arm1_1",
				CubeListBuilder.create().texOffs(14, 30).mirror()
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(53, 79).addBox(1.0F, 10.0F, -1.0F, 1.0F, 2.5F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(10, 67).addBox(-2.0F, 10.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 12.0167F, 0.0F));

		PartDefinition razor1 = arm1_1.addOrReplaceChild("razor1", CubeListBuilder.create().texOffs(21, 43).addBox(
				-0.5F, -7.5F, -2.4F, 1.0F, 8.0F, 1.4F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.5F, 1.5F));

		PartDefinition razor1_2 = razor1.addOrReplaceChild("razor1_2", CubeListBuilder.create(),
				PartPose.offset(0.0F, -7.5F, 0.5F));

		PartDefinition arm2 = body.addOrReplaceChild("arm2",
				CubeListBuilder.create().texOffs(24, 73)
						.addBox(-1.0F, 0.0167F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 79)
						.addBox(-1.0F, 0.0167F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 64)
						.addBox(-1.0F, 1.0167F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(30, 8)
						.addBox(-1.0F, 2.0167F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-8.0F, -13.4833F, 1.5F));

		PartDefinition arm1_2 = arm2.addOrReplaceChild("arm1_2",
				CubeListBuilder.create().texOffs(16, 64)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(54, 79)
						.addBox(-2.0F, 10.0F, -1.0F, 1.0F, 2.5F, 2.0F, new CubeDeformation(0.0F)).texOffs(46, 86)
						.addBox(1.0F, 10.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 12.0167F, 0.0F));

		PartDefinition razor2 = arm1_2.addOrReplaceChild("razor2",
				CubeListBuilder.create().texOffs(1, 86).mirror()
						.addBox(-0.5F, -7.5F, -2.4F, 1.0F, 8.0F, 1.4F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 8.5F, 1.5F));

		PartDefinition razor1_3 = razor2.addOrReplaceChild("razor1_3", CubeListBuilder.create(),
				PartPose.offset(0.0F, -7.5F, 0.5F));

		PartDefinition neck = body.addOrReplaceChild("neck",
				CubeListBuilder.create().texOffs(19, 23)
						.addBox(0.9178F, -4.0355F, -0.5858F, 0.5F, 4.0F, 0.5F, new CubeDeformation(0.0F))
						.texOffs(15, 62).addBox(-1.1F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1F, -14.9667F, 1.5F, 0.4363F, 0.0F, 0.0F));

		PartDefinition h_head = neck.addOrReplaceChild("h_head", CubeListBuilder.create().texOffs(6, 55)
				.addBox(-3.0F, -5.9333F, -3.0667F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(79, 9)
				.addBox(-2.0F, -5.4333F, -4.0667F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(43, 54)
				.addBox(-1.0F, -4.9333F, -5.0667F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 41)
				.addBox(-1.0F, -6.9333F, -4.0667F, 2.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 41)
				.addBox(-1.0F, -6.9333F, -4.0667F, 2.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(79, 7)
				.addBox(1.0F, -5.4333F, -4.0667F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 55).mirror()
				.addBox(1.0F, -5.9333F, -3.0667F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(27, 12).addBox(-1.0F, -5.9333F, 2.9333F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.1F, -4.0667F, 0.0667F, -0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}