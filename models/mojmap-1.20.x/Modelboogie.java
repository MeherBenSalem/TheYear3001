// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelboogie<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "boogie"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	private final ModelPart righth;
	private final ModelPart leftarm;
	private final ModelPart leftforearm;
	private final ModelPart rightarm;
	private final ModelPart rightforearm;

	public Modelboogie(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.leftleg = this.body.getChild("leftleg");
		this.rightleg = this.body.getChild("rightleg");
		this.righth = this.rightleg.getChild("righth");
		this.leftarm = this.body.getChild("leftarm");
		this.leftforearm = this.leftarm.getChild("leftforearm");
		this.rightarm = this.body.getChild("rightarm");
		this.rightforearm = this.rightarm.getChild("rightforearm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(15, 0).mirror()
						.addBox(-2.0F, -0.75F, -2.25F, 4.0F, 4.0F, 4.5F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(14, 7).addBox(-1.0F, -1.75F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 18.75F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(1, 0).mirror()
						.addBox(-6.5F, -5.0F, 0.0F, 7.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(3.0F, -1.75F, -3.0F));

		PartDefinition leftleg = body.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F,
				-1.5F, -3.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.75F, 0.0F));

		PartDefinition rightleg = body.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(0, 22).addBox(
				-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 3.75F, 0.0F));

		PartDefinition righth = rightleg.addOrReplaceChild("righth", CubeListBuilder.create().texOffs(0, 22).addBox(
				2.0F, -6.0F, -6.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(-0.01F)), PartPose.offset(-3.5F, 4.5F, 2.5F));

		PartDefinition leftarm = body.addOrReplaceChild("leftarm",
				CubeListBuilder.create().texOffs(27, 12).mirror()
						.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(2.3535F, 0.6036F, 0.0F));

		PartDefinition leftforearm = leftarm.addOrReplaceChild("leftforearm", CubeListBuilder.create(),
				PartPose.offset(1.5116F, 0.0F, 0.0F));

		PartDefinition leftforearm_r1 = leftforearm.addOrReplaceChild("leftforearm_r1",
				CubeListBuilder.create().texOffs(24, 12).addBox(-0.75F, -0.5F, -0.5F, 1.25F, 1.25F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.4884F, -0.1F, 0.0F, -0.0309F, -0.0308F, 0.0005F));

		PartDefinition leftforearm_r2 = leftforearm.addOrReplaceChild("leftforearm_r2",
				CubeListBuilder.create().texOffs(16, 3).mirror()
						.addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.4884F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(26, 12).addBox(
				-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.25F, 0.0F));

		PartDefinition rightforearm = rightarm.addOrReplaceChild("rightforearm", CubeListBuilder.create(),
				PartPose.offset(-2.0116F, 0.0F, 0.0F));

		PartDefinition rightforearm_r1 = rightforearm.addOrReplaceChild("rightforearm_r1",
				CubeListBuilder.create().texOffs(16, 2).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0116F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition rightforearm_r2 = rightforearm.addOrReplaceChild("rightforearm_r2",
				CubeListBuilder.create().texOffs(25, 12).mirror()
						.addBox(-0.5F, -0.5F, -0.5F, 1.25F, 1.25F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-2.4884F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}