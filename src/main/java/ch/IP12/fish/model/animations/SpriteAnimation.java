package ch.IP12.fish.model.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    //main image as an image view
    public final ImageView imageView;

    //frame count
    private final int count;

    //amount of columns in sprite sheet
    private final int columns;

    //offset from the side of the sprite sheet
    private final int offsetX;
    private final int offsetY;

    //size of a frame
    private final int width;
    private final int height;

    //last frame used
    private int lastIndex;

    public SpriteAnimation(ImageView imageView, int count, int columns, int offsetX, int offsetY, int width, int height, Duration duration) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);

        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    @Override
    protected void interpolate(double v) {
        if (count <= 0 || columns <= 0 || width <= 0 || height <= 0) {
            return;
        }
        final int index = Math.min((int) Math.floor(v * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width  + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
