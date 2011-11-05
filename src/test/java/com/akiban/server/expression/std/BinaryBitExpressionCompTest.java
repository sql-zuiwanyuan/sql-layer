/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.expression.std;

import com.akiban.junit.NamedParameterizedRunner;
import com.akiban.junit.Parameterization;
import com.akiban.junit.ParameterizationBuilder;
import com.akiban.server.expression.ExpressionComposer;
import java.util.Collection;
import org.junit.runner.RunWith;

@RunWith(NamedParameterizedRunner.class)
public class BinaryBitExpressionCompTest extends ComposedExpressionTestBase
{
    private final ExpressionComposer composer;

    public BinaryBitExpressionCompTest (ExpressionComposer composer)
    {
        this.composer = composer;
    }

    @NamedParameterizedRunner.TestParameters
    public static Collection<Parameterization> params()
    {
        ParameterizationBuilder pb = new ParameterizationBuilder();

        param(pb, "&", BinaryBitExpression.B_AND_COMPOSER);
        param(pb, "|", BinaryBitExpression.B_OR_COMPOSER);
        param(pb, "^", BinaryBitExpression.B_XOR_COMPOSER);
        param(pb, "<<", BinaryBitExpression.LEFT_SHIFT_COMPOSER);
        param(pb, ">>", BinaryBitExpression.RIGHT_SHIFT_COMPOSER);
        
        return pb.asList();
    }
    
    private static void param(ParameterizationBuilder pb, String name, ExpressionComposer c)
    {
        pb.add(name, c);
    }

    @Override
    protected int childrenCount()
    {
        return 2;
    }

    @Override
    protected ExpressionComposer getComposer()
    {
        return composer;
    }
}
