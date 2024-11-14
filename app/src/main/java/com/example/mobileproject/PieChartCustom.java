package com.example.mobileproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartCustom extends View {

    private int[] dataPoints;
    private String[] labels;
    private Paint sectorPaint;
    private Paint labelPaint;

    public PieChartCustom(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Peinture pour les secteurs
        sectorPaint = new Paint();
        sectorPaint.setStyle(Paint.Style.FILL);

        // Peinture pour les étiquettes
        labelPaint = new Paint();
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTextSize(30f);
    }

    public void setData(int[] dataPoints, String[] labels) {
        this.dataPoints = dataPoints;
        this.labels = labels;
        invalidate(); // Redessiner le graphique avec les nouvelles données
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.length == 0) return;

        int total = 0;
        for (int point : dataPoints) {
            total += point;
        }

        float angleStart = 0f;
        float angleSweep;

        // Dessiner les secteurs du graphique
        for (int i = 0; i < dataPoints.length; i++) {
            angleSweep = (360f * dataPoints[i]) / total;
            sectorPaint.setColor(getSectorColor(i)); // Choisir une couleur pour chaque secteur
            canvas.drawArc(new RectF(100, 100, getWidth() - 100, getHeight() - 100), angleStart, angleSweep, true, sectorPaint);

            // Dessiner les étiquettes au centre des secteurs
            float angleMid = angleStart + angleSweep / 2;
            float x = getWidth() / 2 + (float) (Math.cos(Math.toRadians(angleMid)) * (getWidth() / 4));
            float y = getHeight() / 2 + (float) (Math.sin(Math.toRadians(angleMid)) * (getHeight() / 4));
            canvas.drawText(labels[i], x, y, labelPaint);

            angleStart += angleSweep; // Mettre à jour l'angle de départ pour le prochain secteur
        }
    }

    private int getSectorColor(int index) {
        // Choisir une couleur différente pour chaque secteur
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        return colors[index % colors.length];
    }
}