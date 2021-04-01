package de.hglabor.plugins.duels.pvpbots

import net.minecraft.server.v1_16_R3.*
import java.util.*


open class PathfinderGoalMeleeAttack(var0: EntityCreature, var1: Double, var3: Boolean) : PathfinderGoal() {
    protected val a: EntityCreature = var0
    private var b = 0
    private val d: Double = var1
    private val e: Boolean = var3
    private var f: PathEntity? = null
    private var g = 0
    private var h = 0.0
    private var i = 0.0
    private var j = 0.0
    protected val c = 20
    private var k: Long = 0
    private var range = 9.0
    private var tick = 5
    private val random = Random()

    init {
        this.a(EnumSet.of(Type.MOVE, Type.LOOK))
    }

    override fun a(): Boolean {
        val var0 = this.a.world.time
        return if (var0 - k < 20L) {
            false
        } else {
            k = var0
            val goalTarget = this.a.goalTarget
            if (goalTarget == null) {
                false
            } else if (!goalTarget.isAlive) {
                false
            } else {
                f = this.a.navigation.a(goalTarget, 0)
                if (f != null) {
                    true
                } else {
                    this.a(goalTarget) >= this.a.h(goalTarget.locX(), goalTarget.locY(), goalTarget.locZ())
                }
            }
        }
    }

    override fun b(): Boolean {
        val goalTarget = this.a.goalTarget
        return if (goalTarget == null) {
            false
        } else if (!goalTarget.isAlive) {
            false
        } else if (!e) {
            !this.a.navigation.m()
        } else if (!this.a.a(goalTarget.chunkCoordinates)) {
            false
        } else {
            goalTarget !is EntityHuman || !goalTarget.isSpectator() && !goalTarget.isCreative
        }
    }

    //Target gefunden
    override fun c() {
        this.a.navigation.a(f, d)
        this.a.isAggressive = true
        g = 0
        this.a.controllerJump.jump()
    }

    override fun d() {
        val var0 = this.a.goalTarget
        if (!IEntitySelector.e.test(var0)) {
            this.a.goalTarget = null
        }
        this.a.isAggressive = false
        this.a.navigation.o()
    }

    override fun e() {
        val attacker = this.a.goalTarget ?: return
        this.a.controllerLook.a(attacker, 30.0f, 30.0f)
        val distanceToPlayer = this.a.h(attacker.locX(), attacker.locY(), attacker.locZ())
        --g
        if ((e || this.a.entitySenses.a(attacker)) && g <= 0 && (h == 0.0 && i == 0.0 && j == 0.0 || attacker.h(
                h,
                i, j
            ) >= 1.0 || this.a.random.nextFloat() < 0.05f)
        ) {
            h = attacker.locX()
            i = attacker.locY()
            j = attacker.locZ()
            g = 4 + this.a.random.nextInt(7)
            if (distanceToPlayer > 1024.0) {
                g += 10
            } else if (distanceToPlayer > 256.0) {
                g += 5
            }
            if (!this.a.navigation.a(attacker, d)) {
                g += 15
            }
        }
        b = (b - 1).coerceAtLeast(0)
        this.a(attacker, distanceToPlayer)

//        if (random.nextInt(20) == 2) {
//            this.a.getControllerJump().jump();
//            this.a.getBukkitEntity().getVelocity().add(this.a.getBukkitEntity().getVelocity().multiply(new Vector(2, 1, 2)));
//            Bukkit.broadcastMessage("laufen");
//        }
    }

    protected fun a(var0: EntityLiving?, distanceToPlayer: Double) {
        val attackRadius = range
        // System.out.println(distanceToPlayer); //5
        // System.out.println(attackRadius); //2
        // System.out.println(this.b); //0
        if (distanceToPlayer <= attackRadius && b <= 0) {
            //  System.out.println("ok");
            b = tick
            this.a.swingHand(EnumHand.MAIN_HAND)
            this.a.attackEntity(var0)
        }
    }

    protected fun a(var0: EntityLiving): Double {
        return (this.a.width * 2.0f * this.a.width * 2.0f + var0.width).toDouble()
    }
}