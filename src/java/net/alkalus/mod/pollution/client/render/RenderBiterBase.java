package net.alkalus.mod.pollution.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.alkalus.mod.pollution.BiterMod;
import net.alkalus.mod.pollution.client.model.ModelBiterBase;
import net.alkalus.mod.pollution.entity.base.BaseEntityBiter;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderBiterBase extends RenderLiving
{
    private static final ResourceLocation BiterEyesTextures = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter_eyes.png");
    private static final ResourceLocation SpitterEyesTextures = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/spitter_eyes.png");
    private static final ResourceLocation BiterTextures_1 = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter.png");
    private static final ResourceLocation BiterTextures_2 = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter_large.png");
    private static final ResourceLocation BiterTextures_3 = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter_boss.png");
    private static final ResourceLocation SpitterTextures_1 = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter_spitter.png");
    private static final ResourceLocation SpitterTextures_2 = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter_spitter_large.png");
    private static final ResourceLocation SpitterTextures_3 = new ResourceLocation(BiterMod.MODID+":"+"textures/entity/biter_spitter_boss.png");

    public RenderBiterBase(float aSize){
        super(new ModelBiterBase(), aSize);
        this.setRenderPassModel(new ModelBiterBase());
    }

    protected float getDeathMaxRotation(BaseEntityBiter p_77037_1_)
    {
        return 180.0F;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(BaseEntityBiter aEntity, int p_77032_2_, float p_77032_3_)
    {
        if (p_77032_2_ != 0)
        {
            return -1;
        }
        else
        {
        	ResourceLocation eyes;
        	boolean aSpitter = aEntity.isSpitter();
        	if (aSpitter) {
        		eyes = SpitterEyesTextures;
        	}
        	else {
        		eyes = BiterEyesTextures;
        	}       	
        	
            this.bindTexture(eyes);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (aEntity.isInvisible())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            return 1;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(BaseEntityBiter aEntity){
    	ResourceLocation body;
    	boolean aLarge = aEntity.isLarge();
    	boolean aSpitter = aEntity.isSpitter();
    	boolean aBoss = aEntity.isLeader();
    	if (aSpitter) {
    		if (aBoss) {
    			body = SpitterTextures_3;    			
    		}
    		else {
    			if (!aLarge) {
        			body = SpitterTextures_1;
        		}
        		else {
        			body = SpitterTextures_2;   			
        		}
    		}    		
    	}
		else {
			if (aBoss) {
				body = BiterTextures_3;
			}
			else {
				if (!aLarge) {
					body = BiterTextures_1;
				}
				else {
					body = BiterTextures_2;
				}
			}
		}
        return body;
    }

    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_)
    {
        return this.getDeathMaxRotation((BaseEntityBiter)p_77037_1_);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        return this.shouldRenderPass((BaseEntityBiter)p_77032_1_, p_77032_2_, p_77032_3_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((BaseEntityBiter)p_110775_1_);
    }
}