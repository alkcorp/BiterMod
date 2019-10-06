package net.alkalus.mod.pollution.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelBiterBase extends ModelBase
{
    /** The biter's head box */
    public ModelRenderer biterHead;
    /** The biter's neck box */
    public ModelRenderer biterNeck;
    /** The biter's body box */
    public ModelRenderer biterBody;
    /** Spider's first leg */
    public ModelRenderer biterLeg1;
    /** Spider's second leg */
    public ModelRenderer biterLeg2;
    /** Spider's third leg */
    public ModelRenderer biterLeg3;
    /** Spider's fourth leg */
    public ModelRenderer biterLeg4;
    /** Spider's fifth leg */
    public ModelRenderer biterLeg5;
    /** Spider's sixth leg */
    public ModelRenderer biterLeg6;
    /** Spider's seventh leg */
    public ModelRenderer biterLeg7;
    /** Spider's eight leg */
    public ModelRenderer biterLeg8;

    public ModelBiterBase()
    {
        float f = 0.0F;
        byte b0 = 15;
        this.biterHead = new ModelRenderer(this, 32, 4);
        this.biterHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
        this.biterHead.setRotationPoint(0.0F, (float)b0, -3.0F);
        this.biterNeck = new ModelRenderer(this, 0, 0);
        this.biterNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
        this.biterNeck.setRotationPoint(0.0F, (float)b0, 0.0F);
        this.biterBody = new ModelRenderer(this, 0, 12);
        this.biterBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
        this.biterBody.setRotationPoint(0.0F, (float)b0, 9.0F);
        this.biterLeg1 = new ModelRenderer(this, 18, 0);
        this.biterLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg1.setRotationPoint(-4.0F, (float)b0, 2.0F);
        this.biterLeg2 = new ModelRenderer(this, 18, 0);
        this.biterLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg2.setRotationPoint(4.0F, (float)b0, 2.0F);
        this.biterLeg3 = new ModelRenderer(this, 18, 0);
        this.biterLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg3.setRotationPoint(-4.0F, (float)b0, 1.0F);
        this.biterLeg4 = new ModelRenderer(this, 18, 0);
        this.biterLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg4.setRotationPoint(4.0F, (float)b0, 1.0F);
        this.biterLeg5 = new ModelRenderer(this, 18, 0);
        this.biterLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg5.setRotationPoint(-4.0F, (float)b0, 0.0F);
        this.biterLeg6 = new ModelRenderer(this, 18, 0);
        this.biterLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg6.setRotationPoint(4.0F, (float)b0, 0.0F);
        this.biterLeg7 = new ModelRenderer(this, 18, 0);
        this.biterLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg7.setRotationPoint(-4.0F, (float)b0, -1.0F);
        this.biterLeg8 = new ModelRenderer(this, 18, 0);
        this.biterLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.biterLeg8.setRotationPoint(4.0F, (float)b0, -1.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.biterHead.render(p_78088_7_);
        this.biterNeck.render(p_78088_7_);
        this.biterBody.render(p_78088_7_);
        this.biterLeg1.render(p_78088_7_);
        this.biterLeg2.render(p_78088_7_);
        this.biterLeg3.render(p_78088_7_);
        this.biterLeg4.render(p_78088_7_);
        this.biterLeg5.render(p_78088_7_);
        this.biterLeg6.render(p_78088_7_);
        this.biterLeg7.render(p_78088_7_);
        this.biterLeg8.render(p_78088_7_);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        this.biterHead.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.biterHead.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
        float f6 = ((float)Math.PI / 4F);
        this.biterLeg1.rotateAngleZ = -f6;
        this.biterLeg2.rotateAngleZ = f6;
        this.biterLeg3.rotateAngleZ = -f6 * 0.74F;
        this.biterLeg4.rotateAngleZ = f6 * 0.74F;
        this.biterLeg5.rotateAngleZ = -f6 * 0.74F;
        this.biterLeg6.rotateAngleZ = f6 * 0.74F;
        this.biterLeg7.rotateAngleZ = -f6;
        this.biterLeg8.rotateAngleZ = f6;
        float f7 = -0.0F;
        float f8 = 0.3926991F;
        this.biterLeg1.rotateAngleY = f8 * 2.0F + f7;
        this.biterLeg2.rotateAngleY = -f8 * 2.0F - f7;
        this.biterLeg3.rotateAngleY = f8 * 1.0F + f7;
        this.biterLeg4.rotateAngleY = -f8 * 1.0F - f7;
        this.biterLeg5.rotateAngleY = -f8 * 1.0F + f7;
        this.biterLeg6.rotateAngleY = f8 * 1.0F - f7;
        this.biterLeg7.rotateAngleY = -f8 * 2.0F + f7;
        this.biterLeg8.rotateAngleY = f8 * 2.0F - f7;
        float f9 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_78087_2_;
        float f10 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * p_78087_2_;
        float f11 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * p_78087_2_;
        float f12 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + ((float)Math.PI * 3F / 2F)) * 0.4F) * p_78087_2_;
        float f13 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 0.0F) * 0.4F) * p_78087_2_;
        float f14 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + (float)Math.PI) * 0.4F) * p_78087_2_;
        float f15 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * p_78087_2_;
        float f16 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + ((float)Math.PI * 3F / 2F)) * 0.4F) * p_78087_2_;
        this.biterLeg1.rotateAngleY += f9;
        this.biterLeg2.rotateAngleY += -f9;
        this.biterLeg3.rotateAngleY += f10;
        this.biterLeg4.rotateAngleY += -f10;
        this.biterLeg5.rotateAngleY += f11;
        this.biterLeg6.rotateAngleY += -f11;
        this.biterLeg7.rotateAngleY += f12;
        this.biterLeg8.rotateAngleY += -f12;
        this.biterLeg1.rotateAngleZ += f13;
        this.biterLeg2.rotateAngleZ += -f13;
        this.biterLeg3.rotateAngleZ += f14;
        this.biterLeg4.rotateAngleZ += -f14;
        this.biterLeg5.rotateAngleZ += f15;
        this.biterLeg6.rotateAngleZ += -f15;
        this.biterLeg7.rotateAngleZ += f16;
        this.biterLeg8.rotateAngleZ += -f16;
    }
}