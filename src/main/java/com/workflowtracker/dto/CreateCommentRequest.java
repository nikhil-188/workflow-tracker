package com.workflowtracker.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CreateCommentRequest
{
    @NotNull
    private String content;
}
