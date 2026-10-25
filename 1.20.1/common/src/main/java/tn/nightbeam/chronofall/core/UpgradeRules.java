package tn.nightbeam.chronofall.core;

import java.util.Locale;

/**
 * What the upgrade cards do to a Mech. Each card adds a fixed step up to a cap (1.0 added a random amount with no
 * cap); the Repair Card heals instead of upgrading.
 */
public final class UpgradeRules {
    public enum Card {
        BULK("bulk_upgrade_card", 10.0, 5),
        DAMAGE("damage_upgrade_card", 2.0, 5),
        DEFENSE("defense_upgrade_card", 4.0, 5),
        SPEED("speed_upgrade_card", 0.10, 3),
        REPAIR("repair_card", 20.0, 0);

        private final String item;
        private final double step;
        private final int cap;

        Card(String item, double step, int cap) {
            this.item = item;
            this.step = step;
            this.cap = cap;
        }

        /** The item path of the card. */
        public String item() {
            return item;
        }

        /** Added per card: max health, rider bonus damage, armour, speed fraction; or health healed (Repair). */
        public double step() {
            return step;
        }

        /** How many of this card a mech accepts (0: no limit, the card is used up as a heal). */
        public int cap() {
            return cap;
        }

        public boolean isUpgrade() {
            return cap > 0;
        }

        /** Short name used for the mech's saved upgrade levels. */
        public String key() {
            return name().toLowerCase(Locale.ROOT);
        }

        public static Card byItem(String path) {
            for (Card card : values()) {
                if (card.item.equals(path)) {
                    return card;
                }
            }
            return null;
        }
    }

    public static final double MECH_BASE_HEALTH = 80.0;
    public static final double MECH_BASE_ARMOR = 8.0;
    public static final double MECH_BASE_SPEED = 0.28;
    /** Extra melee damage for a rider before any Damage card. */
    public static final double MECH_BASE_BONUS_DAMAGE = 6.0;

    private UpgradeRules() {
    }

    /** The level after applying one more card, or -1 when the card is capped (the card is then not used up). */
    public static int apply(Card card, int level) {
        if (!card.isUpgrade()) {
            return level;
        }
        return level >= card.cap() ? -1 : level + 1;
    }

    public static double maxHealth(int bulk) {
        return MECH_BASE_HEALTH + Card.BULK.step() * clamp(bulk, Card.BULK);
    }

    public static double armor(int defense) {
        return MECH_BASE_ARMOR + Card.DEFENSE.step() * clamp(defense, Card.DEFENSE);
    }

    public static double speed(int speed) {
        return MECH_BASE_SPEED * (1.0 + Card.SPEED.step() * clamp(speed, Card.SPEED));
    }

    public static double bonusDamage(int damage) {
        return MECH_BASE_BONUS_DAMAGE + Card.DAMAGE.step() * clamp(damage, Card.DAMAGE);
    }

    private static int clamp(int level, Card card) {
        return Math.max(0, Math.min(card.cap(), level));
    }
}
