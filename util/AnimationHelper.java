package com.pharmacy.util;

import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

public class AnimationHelper {

    private static final Random random = new Random();
    private static final Color[] LUXURY_COLORS = {
        Color.web("#10B981"), // Emerald
        Color.web("#0EA5E9"), // Blue
        Color.web("#8B5CF6"), // Lavender
        Color.web("#F43F5E"), // Rose/Coral
        Color.web("#F59E0B"), // Gold/Amber
        Color.web("#0D9488")  // Teal
    };

    private static final Color[] LIGHT_LUXURY_COLORS = {
        Color.web("#0D9488"), // Teal
        Color.web("#0EA5E9"), // Light Blue
        Color.web("#38BDF8"), // Soft Cyan
        Color.web("#2DD4BF")  // Teal/Mint
    };

    /**
     * Adds a luxury animated background with floating soft bubbles to the given pane.
     * @param root The root pane to add bubbles to.
     * @param bubbleCount Number of bubbles to create.
     */
    public static void applyLuxuryBackground(Pane root, int bubbleCount) {
        for (int i = 0; i < bubbleCount; i++) {
            createBubble(root, LUXURY_COLORS, 50, 150, 0.05);
        }
    }

    /**
     * Adds a light-themed luxury animated background for login/register screens.
     * @param root The root pane.
     * @param bubbleCount Number of bubbles.
     */
    public static void applyLightLuxuryBackground(Pane root, int bubbleCount) {
        for (int i = 0; i < bubbleCount; i++) {
            createBubble(root, LIGHT_LUXURY_COLORS, 30, 100, 0.04);
        }
    }

    private static void createBubble(Pane root, Color[] colorPalette, double minRadius, double extraRadius, double baseOpacity) {
        double radius = minRadius + random.nextDouble() * extraRadius;
        Circle bubble = new Circle(radius);
        
        // Random color from provided palette
        Color baseColor = colorPalette[random.nextInt(colorPalette.length)];
        bubble.setFill(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), baseOpacity + random.nextDouble() * 0.08));
        
        // Initial position
        bubble.setCenterX(random.nextDouble() * 1200);
        bubble.setCenterY(random.nextDouble() * 800);
        
        // Blur effect using Java API
        bubble.setEffect(new javafx.scene.effect.GaussianBlur(50));
        bubble.setMouseTransparent(true); // VERY IMPORTANT: Don't block clicks!
        
        root.getChildren().add(0, bubble); // Add to back

        // Floating Animation
        double targetX = random.nextDouble() * 1200;
        double targetY = random.nextDouble() * 800;
        
        TranslateTransition move = new TranslateTransition(Duration.seconds(20 + random.nextInt(40)), bubble);
        move.setToX(targetX - bubble.getCenterX());
        move.setToY(targetY - bubble.getCenterY());
        move.setCycleCount(Animation.INDEFINITE);
        move.setAutoReverse(true);
        move.setInterpolator(Interpolator.EASE_BOTH);

        // Scaling / Breathing Animation
        ScaleTransition scale = new ScaleTransition(Duration.seconds(10 + random.nextInt(20)), bubble);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setCycleCount(Animation.INDEFINITE);
        scale.setAutoReverse(true);
        scale.setInterpolator(Interpolator.EASE_BOTH);

        // Opacity Pulse
        FadeTransition fade = new FadeTransition(Duration.seconds(15 + random.nextInt(25)), bubble);
        fade.setFromValue(bubble.getOpacity());
        fade.setToValue(bubble.getOpacity() * 0.5);
        fade.setCycleCount(Animation.INDEFINITE);
        fade.setAutoReverse(true);

        move.play();
        scale.play();
        fade.play();
    }
}
