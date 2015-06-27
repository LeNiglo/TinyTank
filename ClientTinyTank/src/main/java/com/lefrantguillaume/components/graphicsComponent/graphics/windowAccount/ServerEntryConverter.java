package com.lefrantguillaume.components.graphicsComponent.graphics.windowAccount;

import com.lefrantguillaume.components.networkComponent.ServerEntry;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

/**
 * Created by leniglo on 03/06/15.
 */
public class ServerEntryConverter implements ListBox.ListBoxViewConverter<ServerEntry> {
    @Override
    public void display(@Nonnull final Element element, final ServerEntry serverEntry) {
        final Element text = element.findElementById("#server-name");
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        final Element ip = element.findElementById("#server-ip");
        final TextRenderer ipRenderer = ip.getRenderer(TextRenderer.class);

        if (serverEntry != null) {
            textRenderer.setText(serverEntry.getName());
            ipRenderer.setColor(Color.WHITE);
            ipRenderer.setText(serverEntry.getIp());
        } else {
            textRenderer.setText("");
            ipRenderer.setText("");
        }
    }

    @Override
    public int getWidth(@Nonnull final Element element, final ServerEntry serverEntry) {
        final Element text = element.findElementById("#server-name");
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        final Element ip = element.findElementById("#server-ip");
        final TextRenderer ipRenderer = ip.getRenderer(TextRenderer.class);

        return textRenderer.getFont().getWidth(serverEntry.getName()) + ipRenderer.getFont().getWidth(serverEntry.getIp());
    }
}
