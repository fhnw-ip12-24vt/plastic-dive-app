package ch.IP12.fish.model.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Creates a 2d animation from Spritesheet image
 */
public class SpriteAnimation extends Transition {
    //Spritesheet as an image view
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

    private final Duration duration;

    /**
     * @param imageView Imageview object containing the spritesheet image
     * @param count amount of frames
     * @param columns amount of columns in provided spritesheet
     * @param offsetX offset on the x-axis between frames (including the first)
     * @param offsetY offset on the y-axis between frames (including the first)
     * @param width width of a frame
     * @param height height of a frame
     * @param duration duration of animation
     */
    public SpriteAnimation(ImageView imageView, int count, int columns, int offsetX, int offsetY, int width, int height, Duration duration) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.duration = duration;
        lastIndex = count;

        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);

        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    /**
     * @param spriteAnimation creates a copy of provided spriteAnimation object in its default state
     */
    public SpriteAnimation(SpriteAnimation spriteAnimation) {
        imageView = spriteAnimation.imageView;
        count = spriteAnimation.count;
        columns = spriteAnimation.columns;
        offsetX = spriteAnimation.offsetX;
        offsetY = spriteAnimation.offsetY;
        width = spriteAnimation.width;
        height = spriteAnimation.height;
        duration = spriteAnimation.duration;
        lastIndex = count;

        setCycleDuration(spriteAnimation.duration);
        setInterpolator(Interpolator.LINEAR);

        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    /**
     * Increments position in animation based on column and row count, uses width, height and offsets to determine where frames are
     */
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

    /**
     * @return current position in view of animation
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @return Height of a frame
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return Width of a frame
     */
    public int getWidth() {
        return width;
    }
}
