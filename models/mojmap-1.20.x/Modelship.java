// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelship<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "ship"), "main");
	private final ModelPart submarine;
	private final ModelPart seat;
	private final ModelPart propeller;

	public Modelship(ModelPart root) {
		this.submarine = root.getChild("submarine");
		this.seat = this.submarine.getChild("seat");
		this.propeller = this.submarine.getChild("propeller");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition submarine = partdefinition.addOrReplaceChild("submarine",
				CubeListBuilder.create().texOffs(171, 125)
						.addBox(-7.0F, -16.0F, -29.0F, 38.0F, 18.0F, 35.0F, new CubeDeformation(0.0F)).texOffs(0, 242)
						.addBox(-6.0F, -1.0F, -74.0F, 36.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(85, 322)
						.addBox(30.0F, -11.0F, -74.0F, 2.0F, 11.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(85, 322)
						.mirror().addBox(-8.0F, -11.0F, -74.0F, 2.0F, 11.0F, 32.0F, new CubeDeformation(0.0F))
						.mirror(false).texOffs(5, 126)
						.addBox(-8.0F, -26.0F, -69.0F, 40.0F, 15.0F, 40.0F, new CubeDeformation(0.15F)).texOffs(215, 93)
						.addBox(-6.0F, -11.0F, -74.0F, 36.0F, 11.0F, 8.0F, new CubeDeformation(-0.001F)).texOffs(0, 93)
						.addBox(-33.5F, -14.0F, 5.8025F, 91.0F, 15.0F, 16.0F, new CubeDeformation(0.01F)).texOffs(0, 0)
						.addBox(-46.6675F, 0.9F, -75.1975F, 118.0F, 0.0F, 92.0F, new CubeDeformation(0.0F))
						.texOffs(119, 287).addBox(-8.0F, -21.0F, -42.0F, 40.0F, 21.0F, 13.0F, new CubeDeformation(0.0F))
						.texOffs(171, 179).addBox(0.0F, -21.0F, -8.0F, 24.0F, 24.0F, 38.0F, new CubeDeformation(0.0F))
						.texOffs(115, 184).addBox(1.0F, -20.0F, 30.0F, 22.0F, 22.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-17.0F, 21.0F, 52.0F));

		PartDefinition submarine_r1 = submarine.addOrReplaceChild("submarine_r1",
				CubeListBuilder.create().texOffs(130, 373).addBox(-13.5F, -6.5F, -15.5F, 27.0F, 13.0F, 31.0F,
						new CubeDeformation(-1.0F)),
				PartPose.offsetAndRotation(43.5F, -12.5F, -9.5F, 0.0F, 0.0F, 0.1309F));

		PartDefinition submarine_r2 = submarine.addOrReplaceChild("submarine_r2",
				CubeListBuilder.create().texOffs(130, 373).mirror()
						.addBox(-13.5F, -6.5F, -15.5F, 27.0F, 13.0F, 31.0F, new CubeDeformation(-1.0F)).mirror(false),
				PartPose.offsetAndRotation(-19.5F, -12.5F, -9.5F, 0.0F, 0.0F, -0.1309F));

		PartDefinition submarine_r3 = submarine.addOrReplaceChild("submarine_r3",
				CubeListBuilder.create().texOffs(115, 211).mirror()
						.addBox(-32.0F, -15.0F, -1.0F, 12.0F, 15.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(318, 137).mirror()
						.addBox(-32.0F, -12.0F, -17.0F, 12.0F, 12.0F, 27.0F, new CubeDeformation(0.005F)).mirror(false),
				PartPose.offsetAndRotation(-18.1112F, 1.0F, -7.896F, 0.0F, 0.7854F, 0.0F));

		PartDefinition submarine_r4 = submarine.addOrReplaceChild("submarine_r4",
				CubeListBuilder.create().texOffs(296, 219).mirror()
						.addBox(-6.0F, -7.5F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-39.376F, -4.9002F, 7.6088F, 0.3491F, 0.7854F, 0.0F));

		PartDefinition submarine_r5 = submarine.addOrReplaceChild("submarine_r5",
				CubeListBuilder.create().texOffs(296, 179).mirror()
						.addBox(-32.0F, -12.0F, -17.0F, 21.0F, 12.0F, 27.0F, new CubeDeformation(-0.01F)).mirror(false),
				PartPose.offsetAndRotation(-19.2334F, 1.0F, 2.7961F, 0.0F, -0.3054F, 0.0F));

		PartDefinition submarine_r6 = submarine.addOrReplaceChild("submarine_r6",
				CubeListBuilder.create().texOffs(0, 276).mirror()
						.addBox(-32.0F, -12.0F, -17.0F, 32.0F, 12.0F, 27.0F, new CubeDeformation(0.0088F))
						.mirror(false),
				PartPose.offsetAndRotation(-11.9275F, 1.0F, -15.7F, 0.0F, -0.5236F, 0.0F));

		PartDefinition submarine_r7 = submarine.addOrReplaceChild("submarine_r7",
				CubeListBuilder.create().texOffs(0, 316).mirror()
						.addBox(-32.0F, -12.0F, -17.0F, 6.0F, 12.0F, 36.0F, new CubeDeformation(0.005F)).mirror(false)
						.texOffs(254, 242).mirror()
						.addBox(-26.0F, -11.9F, -17.0F, 26.0F, 0.0F, 36.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(5.586F, 1.0F, -40.408F, 0.0F, -0.6981F, 0.0F));

		PartDefinition submarine_r8 = submarine.addOrReplaceChild("submarine_r8",
				CubeListBuilder.create().texOffs(223, 332).mirror()
						.addBox(-32.0F, -9.0F, -17.0F, 18.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(17.1983F, 1.0F, -52.3195F, 0.0F, -0.4363F, 0.0F));

		PartDefinition submarine_r9 = submarine.addOrReplaceChild("submarine_r9",
				CubeListBuilder.create().texOffs(296, 219).addBox(-6.0F, -7.5F, -6.0F, 12.0F, 10.0F, 12.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(63.376F, -4.9002F, 7.6088F, 0.3491F, -0.7854F, 0.0F));

		PartDefinition submarine_r10 = submarine.addOrReplaceChild("submarine_r10",
				CubeListBuilder.create().texOffs(115, 211)
						.addBox(20.0F, -15.0F, -1.0F, 12.0F, 15.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(318, 137)
						.addBox(20.0F, -12.0F, -17.0F, 12.0F, 12.0F, 27.0F, new CubeDeformation(0.005F)),
				PartPose.offsetAndRotation(42.1112F, 1.0F, -7.896F, 0.0F, -0.7854F, 0.0F));

		PartDefinition submarine_r11 = submarine.addOrReplaceChild("submarine_r11",
				CubeListBuilder.create().texOffs(296, 179).addBox(11.0F, -12.0F, -17.0F, 21.0F, 12.0F, 27.0F,
						new CubeDeformation(-0.01F)),
				PartPose.offsetAndRotation(43.2334F, 1.0F, 2.7961F, 0.0F, 0.3054F, 0.0F));

		PartDefinition submarine_r12 = submarine.addOrReplaceChild("submarine_r12",
				CubeListBuilder.create().texOffs(0, 276).addBox(0.0F, -12.0F, -17.0F, 32.0F, 12.0F, 27.0F,
						new CubeDeformation(0.0088F)),
				PartPose.offsetAndRotation(35.9275F, 1.0F, -15.7F, 0.0F, 0.5236F, 0.0F));

		PartDefinition submarine_r13 = submarine.addOrReplaceChild("submarine_r13",
				CubeListBuilder.create().texOffs(254, 242)
						.addBox(0.0F, -12.0F, -17.0F, 26.0F, 0.0F, 36.0F, new CubeDeformation(0.0F)).texOffs(0, 316)
						.addBox(26.0F, -12.1F, -17.0F, 6.0F, 12.0F, 36.0F, new CubeDeformation(0.005F)),
				PartPose.offsetAndRotation(18.414F, 1.1F, -40.408F, 0.0F, 0.6981F, 0.0F));

		PartDefinition submarine_r14 = submarine.addOrReplaceChild("submarine_r14",
				CubeListBuilder.create().texOffs(223, 332).addBox(14.0F, -9.0F, -17.0F, 18.0F, 9.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.8017F, 1.0F, -52.3195F, 0.0F, 0.4363F, 0.0F));

		PartDefinition submarine_r15 = submarine.addOrReplaceChild("submarine_r15",
				CubeListBuilder.create().texOffs(251, 100)
						.addBox(-4.0F, -2.0F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(250, 100)
						.addBox(2.0F, -2.0F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(247, 100)
						.addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.0F, -9.759F, -62.9253F, 0.2618F, 0.0F, 0.0F));

		PartDefinition submarine_r16 = submarine.addOrReplaceChild("submarine_r16",
				CubeListBuilder.create().texOffs(244, 96).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 2.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.0F, -9.5F, -64.0F, 0.5672F, 0.0F, 0.0F));

		PartDefinition seat = submarine.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(276, 332)
				.addBox(-6.0F, 7.1188F, -8.6876F, 12.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(276, 332)
				.mirror().addBox(12.0F, 7.1188F, -8.6876F, 12.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(3.0F, -11.1188F, -49.3124F));

		PartDefinition seat_r1 = seat.addOrReplaceChild("seat_r1",
				CubeListBuilder.create().texOffs(319, 332).mirror()
						.addBox(-6.0F, -9.0F, -1.5F, 12.0F, 18.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(319, 332).addBox(-24.0F, -9.0F, -1.5F, 12.0F, 18.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(18.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition propeller = submarine.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(154, 322)
				.addBox(-13.0F, -13.0F, -7.3825F, 26.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(12.0F, -9.0F, 41.3825F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		submarine.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}