// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelmech<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "mech"), "main");
	private final ModelPart r1d1;
	private final ModelPart tag_name;
	private final ModelPart left_leg;
	private final ModelPart left_foreleg;
	private final ModelPart left_foot;
	private final ModelPart right_leg;
	private final ModelPart right_foreleg;
	private final ModelPart right_foot;

	public Modelmech(ModelPart root) {
		this.r1d1 = root.getChild("r1d1");
		this.tag_name = this.r1d1.getChild("tag_name");
		this.left_leg = this.r1d1.getChild("left_leg");
		this.left_foreleg = this.left_leg.getChild("left_foreleg");
		this.left_foot = this.left_foreleg.getChild("left_foot");
		this.right_leg = this.r1d1.getChild("right_leg");
		this.right_foreleg = this.right_leg.getChild("right_foreleg");
		this.right_foot = this.right_foreleg.getChild("right_foot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition r1d1 = partdefinition.addOrReplaceChild("r1d1", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-12.0F, -60.0F, -12.0F, 24.0F, 36.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(8, 0)
				.addBox(-10.5F, -60.75F, -10.5F, 21.0F, 33.0F, 21.0F, new CubeDeformation(0.0F)).texOffs(4, 111)
				.addBox(-4.5F, -53.25F, -13.5F, 9.0F, 9.0F, 1.5F, new CubeDeformation(0.0F)).texOffs(76, 69)
				.addBox(4.5F, -53.25F, -14.1F, 1.05F, 9.0F, 2.1F, new CubeDeformation(0.0F)).texOffs(79, 75).mirror()
				.addBox(-5.55F, -53.25F, -14.1F, 1.05F, 9.0F, 2.1F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(67, 71).addBox(-4.35F, -54.3F, -14.1F, 8.85F, 1.05F, 2.1F, new CubeDeformation(0.0F))
				.texOffs(67, 69).addBox(-4.35F, -44.25F, -14.1F, 8.85F, 1.05F, 2.1F, new CubeDeformation(0.0F))
				.texOffs(32, 24).addBox(-11.1F, -24.0F, -6.3F, 22.2F, 3.0F, 12.6F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-9.75F, -24.0F, -9.75F, 19.5F, 4.5F, 19.5F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition tag_name = r1d1.addOrReplaceChild("tag_name", CubeListBuilder.create(),
				PartPose.offset(0.0F, -57.75F, 0.0F));

		PartDefinition left_leg = r1d1.addOrReplaceChild("left_leg", CubeListBuilder.create(),
				PartPose.offset(9.75F, -16.3838F, -4.9408F));

		PartDefinition left_leg_r1 = left_leg.addOrReplaceChild("left_leg_r1",
				CubeListBuilder.create().texOffs(108, 2).mirror()
						.addBox(29.25F, -6.75F, 9.0F, 1.5F, 7.5F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(66, 120).addBox(35.25F, -6.75F, 9.0F, 1.5F, 7.5F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(3, 64).addBox(28.5F, -7.5F, -9.0F, 9.0F, 9.0F, 18.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-29.25F, -0.1162F, 7.9408F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_leg_r2 = left_leg.addOrReplaceChild("left_leg_r2",
				CubeListBuilder.create().texOffs(96, 113).addBox(14.25F, 7.0904F, -4.7928F, 4.5F, 5.4F, 8.25F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-12.75F, -16.2412F, 13.1908F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_foreleg = left_leg.addOrReplaceChild("left_foreleg",
				CubeListBuilder.create().texOffs(101, 110)
						.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(20, 24)
						.addBox(-2.55F, 10.5F, -2.55F, 5.1F, 9.0F, 5.1F, new CubeDeformation(0.0F)),
				PartPose.offset(3.75F, -7.5F, 18.0F));

		PartDefinition left_foot = left_foreleg.addOrReplaceChild("left_foot",
				CubeListBuilder.create().texOffs(86, 112)
						.addBox(-2.85F, 0.0F, -8.0063F, 5.7F, 3.75F, 11.4F, new CubeDeformation(0.0F)).texOffs(31, 18)
						.addBox(-2.85F, 3.75F, -12.8812F, 5.7F, 0.75F, 18.375F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 19.5F, -0.2438F));

		PartDefinition right_leg = r1d1.addOrReplaceChild("right_leg", CubeListBuilder.create(),
				PartPose.offset(-9.75F, -16.3838F, -4.9408F));

		PartDefinition right_leg_r1 = right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create()
				.texOffs(7, 105).mirror().addBox(2.25F, -6.75F, 9.0F, 1.5F, 7.5F, 6.0F, new CubeDeformation(0.0F))
				.mirror(false).texOffs(99, 2).mirror()
				.addBox(-3.75F, -6.75F, 9.0F, 1.5F, 7.5F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(24, 27)
				.mirror().addBox(-4.5F, -7.5F, -9.0F, 9.0F, 9.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-3.75F, -0.1162F, 7.9408F, 0.3927F, 0.0F, 0.0F));

		PartDefinition right_leg_r2 = right_leg.addOrReplaceChild("right_leg_r2",
				CubeListBuilder.create().texOffs(97, 2).mirror()
						.addBox(-18.75F, 7.0904F, -4.7928F, 4.5F, 5.4F, 8.25F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(12.75F, -16.2412F, 13.1908F, 0.3927F, 0.0F, 0.0F));

		PartDefinition right_foreleg = right_leg.addOrReplaceChild("right_foreleg",
				CubeListBuilder.create().texOffs(99, 110).mirror()
						.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(20, 24).mirror()
						.addBox(-2.55F, 10.5F, -2.55F, 5.1F, 9.0F, 5.1F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(-3.75F, -7.5F, 18.0F));

		PartDefinition right_foot = right_foreleg.addOrReplaceChild("right_foot", CubeListBuilder.create()
				.texOffs(90, 111).mirror().addBox(-2.85F, 0.0F, -8.0063F, 5.7F, 3.75F, 11.4F, new CubeDeformation(0.0F))
				.mirror(false).texOffs(31, 18).mirror()
				.addBox(-2.85F, 3.75F, -12.8812F, 5.7F, 0.75F, 18.375F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 19.5F, -0.2438F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		r1d1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}