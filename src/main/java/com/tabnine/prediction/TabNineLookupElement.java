package com.tabnine.prediction;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.tabnine.contracts.ResultEntry;
import com.tabnine.selections.CompletionOrigin;
import org.jetbrains.annotations.NotNull;

public class TabNineLookupElement extends LookupElement {
    public final String oldPrefix;
    public final String newPrefix;
    public final String oldSuffix;
    public final String newSuffix;
    public final int index;

    public String completionPrefix;
    public String cursorPrefix;
    public String cursorSuffix;
    public CompletionOrigin origin;
    public String detail = null;
    boolean deprecated = false;

    public TabNineLookupElement(int index, CompletionOrigin origin, String oldPrefix, String newPrefix, String oldSuffix, String newSuffix) {
        this.index = index;
        this.origin = origin;
        this.oldPrefix = oldPrefix;
        this.newPrefix = newPrefix;
        this.oldSuffix = oldSuffix;
        this.newSuffix = newSuffix;
    }

    @Override
    @NotNull
    public String getLookupString() {
        return this.newPrefix;
    }

    @Override
    public void handleInsert(@NotNull InsertionContext context) {
        int end = context.getTailOffset();
        context.getDocument().insertString(end + this.oldSuffix.length(), this.newSuffix);
        context.getDocument().deleteString(end, end + this.oldSuffix.length());
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) {
        if (this.detail != null) {
            presentation.setTypeText(this.detail);
        }
        presentation.setItemTextBold(true);
        presentation.setStrikeout(this.deprecated);
        presentation.setItemText(this.newPrefix);
    }

    public void copyLspFrom(ResultEntry r) {
        this.detail = r.detail;
        if (r.deprecated != null) {
            this.deprecated = r.deprecated;
        }
    }

    public CompletionOrigin getOrigin() {
        return origin;
    }

    public TabNineLookupElement withCompletionPrefix(String prefix) {
        this.completionPrefix = prefix;

        return this;
    }

    public TabNineLookupElement withCursorPrefix(String cursorPrefix) {
        this.cursorPrefix = cursorPrefix;

        return this;
    }

    public TabNineLookupElement withCursorSuffix(String cursorSuffix) {
        this.cursorSuffix = cursorSuffix;

        return this;
    }
}
