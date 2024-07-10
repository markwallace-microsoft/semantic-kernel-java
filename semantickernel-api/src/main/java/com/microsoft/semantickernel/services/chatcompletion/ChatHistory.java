// Copyright (c) Microsoft. All rights reserved.
package com.microsoft.semantickernel.services.chatcompletion;

import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.microsoft.semantickernel.services.chatcompletion.message.ChatMessageTextContent;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * Provides a history of messages between the User, Assistant and System
 */
public class ChatHistory implements Iterable<ChatMessageContent<?>> {

    private final List<ChatMessageContent<?>> chatMessageContents;

    /**
     * The default constructor
     */
    public ChatHistory() {
        this((String) null);
    }

    /**
     * Constructor that adds the given system instructions to the chat history.
     *
     * @param instructions The instructions to add to the chat history
     */
    public ChatHistory(@Nullable String instructions) {
        this.chatMessageContents = new ArrayList<>();
        if (instructions != null) {
            this.chatMessageContents.add(
                ChatMessageTextContent.systemMessage(instructions));
        }
    }

    /**
     * Constructor that adds the given chat message contents to the chat history.
     *
     * @param chatMessageContents The chat message contents to add to the chat history
     */
    public ChatHistory(List<? extends ChatMessageContent> chatMessageContents) {
        this.chatMessageContents = new ArrayList(chatMessageContents);
    }

    /**
     * Get the chat history
     *
     * @return List of messages in the chat
     */
    public List<ChatMessageContent<?>> getMessages() {
        return Collections.unmodifiableList(chatMessageContents);
    }

    /**
     * Get last message
     *
     * @return The most recent message in chat
     */
    public Optional<ChatMessageContent<?>> getLastMessage() {
        if (chatMessageContents.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(chatMessageContents.get(chatMessageContents.size() - 1));
    }

    /**
     * Add all messages from the given chat history to this chat history
     *
     * @param value The chat history to add to this chat history
     */
    public void addAll(ChatHistory value) {
        this.chatMessageContents.addAll(value.getMessages());
    }

    /**
     * Create an {@code Iterator} from the chat history.
     */
    @Override
    public Iterator<ChatMessageContent<?>> iterator() {
        return chatMessageContents.iterator();
    }

    /**
     * Perform the given action for each message in the chat history
     *
     * @param action The action to perform for each message in the chat history
     */
    @Override
    public void forEach(Consumer<? super ChatMessageContent<?>> action) {
        chatMessageContents.forEach(action);
    }

    /**
     * Create a {@code Spliterator} from the chat history
     */
    @Override
    public Spliterator<ChatMessageContent<?>> spliterator() {
        return chatMessageContents.spliterator();
    }

    /**
     * Add a message to the chat history
     *
     * @param authorRole The role of the author of the message
     * @param content    The content of the message
     * @param encoding   The encoding of the message
     * @param metadata   The metadata of the message
     */
    public void addMessage(AuthorRole authorRole, String content, Charset encoding,
        FunctionResultMetadata metadata) {
        chatMessageContents.add(
            ChatMessageTextContent.builder()
                .withAuthorRole(authorRole)
                .withContent(content)
                .withEncoding(encoding)
                .withMetadata(metadata)
                .build());
    }

    /**
     * Add a message to the chat history
     *
     * @param authorRole The role of the author of the message
     * @param content    The content of the message
     */
    public void addMessage(AuthorRole authorRole, String content) {
        chatMessageContents.add(
            ChatMessageTextContent.builder()
                .withAuthorRole(authorRole)
                .withContent(content)
                .build());
    }

    /**
     * Add a message to the chat history
     *
     * @param content The content of the message
     */
    public void addMessage(ChatMessageContent<?> content) {
        chatMessageContents.add(content);
    }

    /**
     * Add a user message to the chat history
     *
     * @param content The content of the user message
     */
    public void addUserMessage(String content) {
        addMessage(AuthorRole.USER, content);
    }

    /**
     * Add an assistant message to the chat history
     *
     * @param content The content of the assistant message
     */
    public void addAssistantMessage(String content) {
        addMessage(AuthorRole.ASSISTANT, content);
    }

    /**
     * Add an system message to the chat history
     *
     * @param content The content of the system message
     */
    public void addSystemMessage(String content) {
        addMessage(AuthorRole.SYSTEM, content);
    }

    public void addAll(List<ChatMessageContent<?>> messages) {
        chatMessageContents.addAll(messages);
    }
}
